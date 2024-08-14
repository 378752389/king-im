package com.king.im.server;

import com.king.im.common.utils.RedisUtils;
import com.king.im.server.ws.WSServerStarter;
import lombok.Getter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class ServerBootstrap implements CommandLineRunner {

    @Getter
    private static volatile Long serverId;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private WSServerStarter WSServerStarter;

    private static final String MAX_SERVER_ID = "max_server_id";

    @Override
    public void run(String... args) throws Exception {
        serverId = redisUtils.incr(MAX_SERVER_ID);
        WSServerStarter.start();
    }
}
