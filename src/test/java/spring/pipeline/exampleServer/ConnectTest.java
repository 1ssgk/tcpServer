package spring.pipeline.exampleServer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import spring.pipeline.exampleServer.client.SampleClientHandler;
import spring.pipeline.exampleServer.dto.BaseDto;
import spring.pipeline.exampleServer.inbound.ReceiveDataUpdater;
import spring.pipeline.exampleServer.packet.PacketType;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class ConnectTest {

    // 클라이언트
    Bootstrap clientBootstrap = new Bootstrap();
    NioEventLoopGroup clientEventLoopGroup = new NioEventLoopGroup();
    BlockingQueue<String> clientResponseQueue = new LinkedBlockingQueue<>();
    BlockingQueue<Byte> clientResponseQueue2 = new LinkedBlockingQueue<>();
    Channel clientChannel;

    @BeforeEach
    @SneakyThrows
    public void beforeEach() throws InterruptedException {
        clientSetupAndConnect();
        waitForServerServiceActive();
    }

    private void waitForServerServiceActive() throws InterruptedException {
        Thread.sleep(100);
    }

    @AfterEach
    @SneakyThrows
    public void afterEach() {
        clientEventLoopGroup.shutdownGracefully();
    }

    private void clientSetupAndConnect() throws InterruptedException {
        clientBootstrap.group(clientEventLoopGroup)
                .channel(NioSocketChannel.class)
                .remoteAddress("127.0.0.1", 8888)
                .handler(new ChannelInitializer<>() {
                    @Override
                    protected void initChannel(Channel ch) {
                        ch.pipeline()
                                // Inbound
//                                .addLast(new FixedLengthFrameDecoder(4))
//                                .addLast(new StringDecoder())
//                                .addLast(new ReceiveDataUpdater(clientResponseQueue))

                                // Outbound
//                                .addLast(new StringEncoder());

                                .addLast(new ByteArrayDecoder())
                                .addLast(new ReceiveDataUpdater(clientResponseQueue));


                    }
                });

        clientChannel = clientBootstrap.connect().sync().channel();
    }


    @Test
    @DisplayName("메시지 전송-응답 테스트")
    void simpleResponseTest() throws InterruptedException {
        // Given : 연결된 서버, 클라이언트

        byte[] bytes2 = "TEST".getBytes();

        BaseDto baseDto = new BaseDto("test", 12);
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(baseDto);
            byte[] body = json.getBytes();

            System.out.println("전송되는 바이트 : "+body.length);

            char STR_DIV = '\f';
            byte strDiv = (byte) STR_DIV;

            ByteBuf buffer = Unpooled.buffer();
            buffer.writeByte(PacketType.In); // packet type
            buffer.writeBytes(body); // body
            buffer.writeByte(strDiv);
            buffer.writeBytes(bytes2);


            //When : 클라이언트에서 메시지 전송
            clientChannel.writeAndFlush((buffer));

            // Then : 서버로부터 응답 수신
            Byte response = clientResponseQueue2.poll(100, TimeUnit.MILLISECONDS);
            //Assertions.assertEquals(fixedResponse, response);


            System.out.println("엥 :: " + clientChannel.read());


            System.out.println("response :: " + response);

            //clientChannel.close();
            Thread.sleep(new Random().nextInt(2));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

}
