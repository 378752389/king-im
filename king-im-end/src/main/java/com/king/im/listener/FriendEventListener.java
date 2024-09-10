package com.king.im.listener;

import com.king.im.client.IMSender;
import com.king.im.client.SendMessageFactory;
import com.king.im.client.domain.SendMessage;
import com.king.im.client.domain.type.ReceiverInfo;
import com.king.im.client.domain.type.SenderInfo;
import com.king.im.listener.event.FriendEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class FriendEventListener {

    @Resource
    private IMSender imSender;

    @EventListener(classes = FriendEvent.class)
    @Async
    public void inviteGroupMsg(FriendEvent event) {
        long peerId = event.getPeerId();
        long userId = event.getUserId();
        String notice = event.getNotice();

        SenderInfo senderInfo = new SenderInfo(0L, 0);
        ReceiverInfo receiverInfo1 = new ReceiverInfo(peerId);
        SendMessage sendMessage1 = SendMessageFactory.buildNoticeMessage(userId, 1, senderInfo, receiverInfo1, notice);
        imSender.send(sendMessage1);

        ReceiverInfo receiverInfo2 = new ReceiverInfo(userId);
        SendMessage sendMessage2 = SendMessageFactory.buildNoticeMessage(peerId, 1, senderInfo, receiverInfo2, notice);
        imSender.send(sendMessage2);
    }
}
