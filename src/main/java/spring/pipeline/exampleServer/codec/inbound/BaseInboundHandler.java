package spring.pipeline.exampleServer.codec.inbound;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import spring.pipeline.exampleServer.packet.Packet;
import spring.pipeline.exampleServer.packet.PacketDispatcher;

@Slf4j
@Component
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class BaseInboundHandler extends ChannelInboundHandlerAdapter {

    private final PacketDispatcher packetDispatcher;

    @PostConstruct
    public void init() {
        log.info("BaseHandler init");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Netty Server Connected");
        ctx.fireChannelActive();
    }

    // 데이터 수신에 대한 이벤트 처리
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("3. BaseInboundHandler START");
        if (msg instanceof Packet) {
            Packet packet = (Packet) msg;
            log.info("Packet ={}", msg);

            // 들어온 packet에 따른 작업 분배
            packetDispatcher.dispatch(ctx, packet);
        }

        log.info("============================");
    }

    // 채널 파이프라인데 대한 이벤트를 처리
    // channelRead 의 이벤트 처리 완료 후 자동으로 수행됨
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("BaseInboundHandler channelReadComplete");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace(); // 예외 stack trace를 출력
        ctx.close(); // 채널을 닫음
    }
}
