package spring.pipeline.exampleServer.codec.outbound;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import spring.pipeline.exampleServer.packet.Packet;
import spring.pipeline.exampleServer.packet.PacketType;

@Slf4j
@Component
@ChannelHandler.Sharable
public class PacketEncoder extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        log.info("2. PacketEncoder START");
        log.info("object ={}", msg);
//        Packet responsePacket = Packet.builder()
//                .type(PacketType.Out)
//                .body((byte[]) msg)
//                .build();
//
//        ObjectMapper mapper = new ObjectMapper();
//        String result = mapper.writeValueAsString(responsePacket);
//        log.info("result ={}",result);
        log.info("============================");

        ctx.write(msg, promise);
    }
}
