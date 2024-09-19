package com.king.im.client;

import com.king.im.client.domain.SendMessage;
import com.king.im.client.domain.message.BaseMessage;
import com.king.im.client.domain.type.ReceiverInfo;
import com.king.im.client.domain.type.SenderInfo;
import com.king.im.common.domain.RecMessage;
import com.king.im.common.domain.type.IMUserInfo;
import com.king.im.server.session.GlobalSessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
@Slf4j
public class IMSender implements ISender {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private GlobalSessionManager globalSessionManager;

    @Override
    public void send(SendMessage sendMessage) {
        RecMessage recMessage = buildRecMessage(sendMessage);
        //todo
        ReceiverInfo receiverInfo = sendMessage.getReceiverInfo();
        List<Long> receiverIds = receiverInfo.getReceiverIds();
        List<Integer> terminals = receiverInfo.getReceiveTerminalTypes();

        // 接受者信息收集
        for (Long receiverId : receiverIds) {
            for (Integer terminal : terminals) {
                IMUserInfo receiver = new IMUserInfo(receiverId, terminal);
                Long serverId = globalSessionManager.getServerId(receiverId, terminal);
                if (serverId != null) {
                    String channel = "message:" + serverId;
                    recMessage.setReceiver(receiver);
                    redisTemplate.convertAndSend(channel, recMessage);
                } else {
                    // todo 离线消息处理
                }
            }
        }

    }





    /**
     * 填充模型数据（不包括接收人信息）
     * @param sendMessage
     * @return
     */
    public RecMessage buildRecMessage(SendMessage sendMessage) {
        SenderInfo senderInfo = sendMessage.getSenderInfo();

        RecMessage recMessage = new RecMessage();

        recMessage.setSender(new IMUserInfo(senderInfo.getUid(), senderInfo.getTerminal()));
        //todo 填充接受者信息
//        recMessage.setReceiver(new IMUserInfo());
        recMessage.setChatType(sendMessage.getSendType());
        recMessage.setMsgId(sendMessage.getMsgId());
        recMessage.setStatus(sendMessage.getStatus());

        BaseMessage message = sendMessage.getMessage();
        recMessage.setChatId(message.getChatId());
        recMessage.setSendTime(message.getSendTime());
        recMessage.setReferMsgId(message.getReferMsgId());
        recMessage.setAtUids(message.getAtUids());
        recMessage.setContent(message.getText());
        recMessage.setExtra(message.getExtra());
        recMessage.setType(sendMessage.getMessageType().getType());

        return recMessage;
    }
}
