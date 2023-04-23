package spring.pipeline.exampleServer.codec.inbound;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import spring.pipeline.exampleServer.packet.Packet;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * ChannelInboundHandler
 */

@Slf4j
@Component
@ChannelHandler.Sharable
public class PacketDecoder extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("2. PacketDecoder START");
        // decode packet
        byte [] received = (byte[]) msg;

        // get packet class
        Packet packet = Packet.builder()
                .type(received[0])
                .body(getBody(received))
                .build();

        log.info("============================");
        ctx.fireChannelRead(packet);
    }

    private byte[] getBody(byte[] received) {
//        byte[] body = new byte[received.length - 1];
//        System.arraycopy(received, 1, body, 0, body.length);

        byte[] body = Arrays.copyOfRange(received, 1, received.length);
        return body;
    }
}
