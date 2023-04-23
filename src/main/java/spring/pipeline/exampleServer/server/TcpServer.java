package spring.pipeline.exampleServer.server;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TcpServer {
    private final NettyServer nettyServer;

    @PostConstruct
    public void init() throws InterruptedException{
        nettyServer.run();
    }

    @PreDestroy
    public void destroy() throws Exception{
        nettyServer.stop();
    }
}
