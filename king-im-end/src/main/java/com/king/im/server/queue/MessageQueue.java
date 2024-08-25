package com.king.im.server.queue;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.king.im.common.utils.JSONUtils;
import com.king.im.common.utils.RedisUtils;
import com.king.im.server.ServerBootstrap;
import com.king.im.server.domain.ReceiveMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class MessageQueue {

    @Resource
    private RedisUtils redisUtils;
    @Resource
    private JSONUtils jsonUtils;

    public ReceiveMessage take() {
        Object o = redisUtils.brpop(getKey(ServerBootstrap.getServerId()), 10, TimeUnit.SECONDS);
        return jsonUtils.convert(o, new TypeReference<ReceiveMessage>() {
        });
    }

    public void put(Long serverId, ReceiveMessage receiveMessage) {
        redisUtils.lpush(getKey(serverId), receiveMessage);
    }

    private String getKey(Long serverId) {
        return "message:" + serverId;
    }
}
