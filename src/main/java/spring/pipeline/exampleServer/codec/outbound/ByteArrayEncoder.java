package spring.pipeline.exampleServer.codec.outbound;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 메세지를 아웃바운드 데이터로 인코딩하는 클래스이다.
 */

@Slf4j
@Component
@ChannelHandler.Sharable
public class ByteArrayEncoder extends MessageToMessageEncoder<byte[]> {
    /**
     * encode() 는 구현해야하는 유일한 메소드이다.
     * write() 로 기록한 각 메세지는 encode() 로 전달된 후
     * 하나 이상의 아웃바운드 메세지로 인코딩된다.
     * 그 후 파이프라인의 다음 ChannelOutboundHandler 로 전달된다.
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, byte[] msg, List<Object> out) throws Exception {
        log.info("1. ByteArrayEncoder START");
        log.info("byte[] -> ByteBuf");
        log.info("============================");

        out.add(Unpooled.wrappedBuffer(5,msg));
    }
}
