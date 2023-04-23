package spring.pipeline.exampleServer.packet;

import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import spring.pipeline.exampleServer.service.CustomerService;

@Slf4j
@Component
@RequiredArgsConstructor
public class PacketDispatcher {

    private final CustomerService customerService;

    public void dispatch(ChannelHandlerContext ctx, Packet packet) {
        // 타입에 따라 작업 구분
        switch (packet.getType()) {
            case PacketType.HeartBeat: {
                "hearBeat".getBytes();
                break;
            }
            case PacketType.In: {
                customerService.in(ctx, packet);

                break;
            }
            case PacketType.Out: {
                customerService.out(ctx, packet);
                break;
            }
            default: {
                ctx.writeAndFlush("작업 불가");
                ctx.close();
                break;
            }
        }
    }
}
