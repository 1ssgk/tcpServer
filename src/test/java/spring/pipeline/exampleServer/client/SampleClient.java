package spring.pipeline.exampleServer.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SampleClient {

    public static BlockingQueue<String> responseMessage = new LinkedBlockingQueue<>();


    public static void main(String[] args) throws Exception {
        String host = "127.0.0.1";
        int port = 8888;//10100
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();

            // 클라이언트는 서버와 달리 서버에 연결된 채널 하나만 존재하기 때문에 이벤트 루프 그룹이 하나다.
            b.group(workerGroup);
            // 클라이언트가 생성하는 채널의 종류를 설정. NIO 소켓 채널로 설정
            b.channel(NioSocketChannel.class);

            // 클라이언트이므로 채널 파이프라인의 설정에 일반 소켓 채널 클래스인 SocketChannel을 설정
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline()
                            .addLast(new ByteArrayDecoder())
                            .addLast(new SampleClientHandler());
                }
            });

            // 비동기 입출력 메서드인 connect 호출. ChannelFuture 객체를 리턴/ 이 객체를 통해 비동기 처리 결과를 확인
            // sync는 ChannelFuture 객체의 요청이 완료될 때까지 대기/ 실패하면 예외 던짐
            ChannelFuture f = b.connect(host, port).sync();

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();

            //String dd = responseMessage.poll(100, TimeUnit.MILLISECONDS);

        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}