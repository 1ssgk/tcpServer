package spring.pipeline.exampleServer.inbound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;


// 테스트용
@Slf4j
@RequiredArgsConstructor
public class ReceiveDataUpdater extends SimpleChannelInboundHandler<Object> {

    private final BlockingQueue<String> messageQueue;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
//        log.info("channelRead0 START -------------");
//        log.info("ChannelHandlerContext ={}", ctx.channel().remoteAddress());
//        log.info("message ={}", msg);
//        log.info("channelRead0 END -------------");

        System.out.println("channelRead0 START -------------");
        System.out.println("ChannelHandlerContext ={}"+ ctx.channel().remoteAddress());
        System.out.println("message ={}"+ msg);
        System.out.println("channelRead0 END -------------");
        byte [] received = (byte[]) msg;
        String responseMessage = new String(received);
        System.out.println("response Message : "+responseMessage);

        messageQueue.put(responseMessage);
        //ctx.fireChannelRead(msg);
    }
}
