package com.king.im.ws;

import com.king.im.common.utils.RedisUtils;
import lombok.Getter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class WebSocketBootstrap implements CommandLineRunner {

    @Getter
    private static volatile Long serverId;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private WebSocketServerStarter webSocketServerStarter;

    private static final String MAX_SERVER_ID = "max_server_id";

    @Override
    public void run(String... args) throws Exception {
        serverId = redisUtils.incr(MAX_SERVER_ID);
        webSocketServerStarter.start();
    }
}
