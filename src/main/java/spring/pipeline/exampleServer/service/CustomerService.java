package spring.pipeline.exampleServer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring.pipeline.exampleServer.dto.BaseDto;
import spring.pipeline.exampleServer.packet.Packet;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class CustomerService {

    String STR_DIV = "\u000C";

    public void in(ChannelHandlerContext ctx, Packet packet) {
        log.info("remoteAddress = {}", ctx.channel().remoteAddress());
        log.info("packet type = {}", packet.getType());

        String requestMessage = new String(packet.getBody(), StandardCharsets.UTF_8);
        String [] msgArr = requestMessage.split(STR_DIV);

        log.info("body ={}", msgArr[0]);
        log.info("other ={}", msgArr[1]);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            BaseDto testDto = objectMapper.readValue(msgArr[0], BaseDto.class);

            byte[] result = "abcdefghijklmnopqrstuvwxyz012".getBytes();

            ctx.writeAndFlush(result);
            ctx.close();

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] out(ChannelHandlerContext ctx, Packet packet) {

        log.info("TestService.test2 START");
        log.info("remoteAddress = {}", ctx.channel().remoteAddress());
        log.info("packet = {}", packet.toString());
        log.info("TestService.test2 END");

        return "out".getBytes();
    }

}
