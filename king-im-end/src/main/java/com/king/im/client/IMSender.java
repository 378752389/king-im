package com.king.im.client;

import com.king.im.client.domain.SendMessage;
import com.king.im.msg.convert.MsgConvert;
import com.king.im.server.ServerBootstrap;
import com.king.im.server.domain.ReceiveMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

@Component
@Slf4j
public class IMSender implements ISender {

    @Resource
    private RedisTemplate<String, Objects> redisTemplate;

    @Override
    public void send(SendMessage sendMessage) {
        // todo
        ReceiveMessage receiveMessage = MsgConvert.buildReceiveMessage(sendMessage);
        redisTemplate.convertAndSend("message:" + ServerBootstrap.getServerId(), receiveMessage);
//        messageQueue.put(ServerBootstrap.getServerId(), receiveMessage);
    }
}
