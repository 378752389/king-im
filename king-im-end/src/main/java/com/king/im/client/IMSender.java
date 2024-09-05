package com.king.im.client;

import com.king.im.common.utils.JSONUtils;
import com.king.im.msg.convert.MsgConvert;
import com.king.im.client.domain.SendMessage;
import com.king.im.client.domain.type.ReceiverInfo;
import com.king.im.server.ServerBootstrap;
import com.king.im.server.domain.ReceiveMessage;
import com.king.im.server.protocol.CMD;
import com.king.im.server.queue.MessageQueue;
import com.king.im.server.session.MessageSender;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class IMSender implements ISender {

    @Resource
    private MessageSender messageSender;
    @Resource
    private MessageQueue messageQueue;
    @Resource
    private JSONUtils jsonUtils;

    @Override
    public void send(SendMessage sendMessage) {
        // todo
        ReceiveMessage receiveMessage = MsgConvert.buildReceiveMessage(sendMessage);
        messageQueue.put(ServerBootstrap.getServerId(), receiveMessage);
    }
}
