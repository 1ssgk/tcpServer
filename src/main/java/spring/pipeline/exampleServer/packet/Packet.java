package spring.pipeline.exampleServer.packet;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Packet<T> {
    private byte type;
    private byte[] body;
    private T packet;
}
