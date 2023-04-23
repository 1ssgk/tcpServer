package spring.pipeline.exampleServer.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;
import spring.pipeline.exampleServer.dto.BaseDto;
import spring.pipeline.exampleServer.packet.PacketType;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

class MsgSender implements Runnable {

    private ChannelHandlerContext ctx;

    public MsgSender(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void run() {

        byte[] bytes2 = "TEST".getBytes();

        BaseDto baseDto = new BaseDto("test", 12);
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(baseDto);

            byte[] body = json.getBytes();

            char STR_DIV = '\f';
            byte strDiv = (byte) STR_DIV;

            ByteBuf buffer = Unpooled.directBuffer(3);
            buffer.writeByte(PacketType.In); // packet type
            buffer.writeBytes(body); // body
            buffer.writeByte(strDiv);
            buffer.writeBytes(bytes2);

            System.out.println("==========================");
            System.out.println("body :" + body);
            System.out.println("bite :" + body.length);
            System.out.println("buffer :" + buffer);
            System.out.println("==========================");
            ctx.writeAndFlush(buffer);

            ByteBuf buffer2 = Unpooled.buffer();
            buffer.writeByte(strDiv);


            Thread.sleep(new Random().nextInt(2));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

public class SampleClientHandler extends ChannelInboundHandlerAdapter {

    private final BlockingQueue<String> responseMessage = new LinkedBlockingQueue<>();

    // 소켓 채널이 최초 활성화되었을 때 실행됨
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client connected");
        // send random data to server when it connected
        new Thread(new MsgSender(ctx)).start();
    }


    // 서버로 수신된 데이터가 있을 때 호출
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws InterruptedException {
        System.out.println("client 로 message 가 들어왔습니다.");
        System.out.println("message : " + msg);

        byte [] received = (byte[]) msg;
        String responseMessage = new String(received);
        this.responseMessage.put(responseMessage);
        System.out.println("response Message : "+responseMessage);
    }

    // 수신된 데이터를 모두 읽었을  호출
    // ChannelRead 가 완료된 후 자동으로 호출된다.
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //String result = this.responseMessage.poll(100, TimeUnit.MILLISECONDS);

        //System.out.println("에엥 : " + result);
        System.out.println("사이즈 : "+this.responseMessage.size());
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("에러발생!!!");
        cause.printStackTrace();
        ctx.close();
    }
}

