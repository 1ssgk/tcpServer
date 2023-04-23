package spring.pipeline.exampleServer.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.pipeline.exampleServer.codec.inbound.ByteArrayDecoder;
import spring.pipeline.exampleServer.codec.outbound.ByteArrayEncoder;
import spring.pipeline.exampleServer.codec.inbound.PacketDecoder;
import spring.pipeline.exampleServer.codec.outbound.PacketEncoder;
import spring.pipeline.exampleServer.codec.inbound.BaseInboundHandler;
import spring.pipeline.exampleServer.codec.outbound.BaseOutboundHandler;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class NettyServer {
//
    private EventLoopGroup acceptGroups = new NioEventLoopGroup(1);
    private EventLoopGroup workGroups = new NioEventLoopGroup();

    private final BaseInboundHandler baseInboundHandler;

    private final BaseOutboundHandler baseOutboundHandler;

    private final PacketDecoder packetDecoder;

    private final PacketEncoder packetEncoder;

    private final ByteArrayDecoder byteArrayDecoder;

    private final ByteArrayEncoder byteArrayEncoder;


    @Bean
    public void run() {

        try {
            // ServerBootstrap 생성
            ServerBootstrap sbs = new ServerBootstrap();
            sbs.group(acceptGroups, workGroups)

                    // NIO 전송 채널을 이용하도록 지정
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,100)

                    // 채널의 Pipeline 을 설정
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            addPipeline(ch);
                        }
                    });

            ChannelFuture f = sbs.bind(8888).sync();


        } catch (Exception e) {
            e.printStackTrace();
//            acceptGroups.shutdownGracefully();
//            workGroups.shutdownGracefully();
        }
    }

    public void stop() {
        acceptGroups.shutdownGracefully();
        workGroups.shutdownGracefully();
    }

    private void addPipeline(SocketChannel ch) {
        ChannelPipeline cp = ch.pipeline();

        // outbound
        cp.addLast(baseOutboundHandler);
        cp.addLast(packetEncoder);
        cp.addLast(byteArrayEncoder);

        // inbound
        cp.addLast(byteArrayDecoder);
        cp.addLast(packetDecoder);
        cp.addLast(baseInboundHandler);


    }
}
