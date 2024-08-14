package com.king.im.sender.queue;

import com.king.im.common.utils.RedisUtils;
import com.king.im.server.protocol.data.ChatData;
import com.king.im.server.ServerBootstrap;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MessageQueue {

    @Resource
    private RedisUtils redisUtils;

    public ChatData take() {
        return (ChatData) redisUtils.rpop(getKey(ServerBootstrap.getServerId()));
    }

    public void put(Long serverId, ChatData chatData) {
        redisUtils.lpush(getKey(serverId), chatData);
    }

    private String getKey(Long serverId) {
        return "message:" + serverId;
    }
}
