package com.king.im.listener;

import com.king.im.client.IMSender;
import com.king.im.client.SendMessageFactory;
import com.king.im.client.domain.SendMessage;
import com.king.im.client.domain.type.ReceiverInfo;
import com.king.im.client.domain.type.SenderInfo;
import com.king.im.listener.event.RoomEvent;
import com.king.im.social.domain.entity.Room;
import com.king.im.social.mapper.RoomMapper;
import com.king.im.user.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RoomEventListener {

    @Resource
    private RoomMapper roomMapper;
    @Resource
    private IMSender imSender;

    @EventListener(classes = RoomEvent.class)
    @Async
    public void inviteGroupMsg(RoomEvent event) {
        long roomId = event.getRoomId();
        int terminalType = event.getTerminalType();
        String notice = event.getNotice();
        List<User> userList = roomMapper.getUserList(roomId);
        Room room = roomMapper.selectById(roomId);

        List<Long> receiverIds = userList.stream().map(User::getId).filter(userId -> !room.getLeaderId().equals(userId)).collect(Collectors.toList());

        SenderInfo senderInfo = new SenderInfo(room.getLeaderId(), terminalType);
        ReceiverInfo receiverInfo = new ReceiverInfo(receiverIds);
        SendMessage sendMessage = SendMessageFactory.buildNoticeMessage(roomId, 2, senderInfo, receiverInfo, notice);

        imSender.send(sendMessage);
    }
}
