package spring.pipeline.exampleServer.codec.inbound;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 *  MessageToMessageDecoder : 메세지를 다른 메시지 유형으로 디코딩
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class ByteArrayDecoder extends MessageToMessageDecoder<ByteBuf> {

    /**
     * 인바운드 메세지를 다른 포맷으로 디코딩할 때마다 호출된다.
     * 디코딩된 메세지는 파이프라인의 다음 ChannelInboundHandler 로 전달된다.
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        log.info("1. ByteArrayDecoder START");
        log.info("ByteBuf -> byte[]");

        byte[] array = new byte[msg.readableBytes()];
        msg.getBytes(0, array);
        log.info("============================");
        out.add(array);
    }
}
