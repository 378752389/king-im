package com.king.im.listener;

import com.king.im.listener.event.MsgEvent;
import com.king.im.msg.convert.MsgConvert;
import com.king.im.msg.domain.entity.Msg;
import com.king.im.client.IMSender;
import com.king.im.client.domain.SendMessage;
import com.king.im.common.enums.ChatTypeEnum;
import com.king.im.client.domain.type.ReceiverInfo;
import com.king.im.social.mapper.RoomMapper;
import com.king.im.user.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class MsgEventListener {
    @Resource
    private RoomMapper roomMapper;


    @Resource
    private IMSender imSender;

    /**
     * 转发在线消息
     *
     * @param event
     */
    @EventListener(classes = MsgEvent.class)
    public void sendTo(MsgEvent event) {
        Msg msg = event.getMsg();
        SendMessage sendMessage = MsgConvert.buildSendMessage(msg);
        if (ChatTypeEnum.GROUP.getType().equals(sendMessage.getSendType())) {
            List<User> userList = roomMapper.getUserList(msg.getRoomId());
            List<Long> userIds = userList.stream().map(User::getId).filter(userId -> !userId.equals(msg.getFromUid())).collect(Collectors.toList());
            sendMessage.setReceiverInfo(new ReceiverInfo(userIds));
        } else if (ChatTypeEnum.SINGLE.getType().equals(sendMessage.getSendType())) {
            sendMessage.setReceiverInfo(new ReceiverInfo(msg.getToUid()));
        }

        imSender.send(sendMessage);

    }

}
