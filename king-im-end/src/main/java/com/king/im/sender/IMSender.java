package com.king.im.sender;

import com.king.im.common.utils.JSONUtils;
import com.king.im.msg.convert.MsgConvert;
import com.king.im.sender.domain.SendMessage;
import com.king.im.sender.domain.type.ReceiverInfo;
import com.king.im.sender.protocol.IMCMD;
import com.king.im.sender.protocol.data.ChatData;
import com.king.im.ws.session.MessageSender;
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
    private JSONUtils jsonUtils;

    @Override
    public void send(SendMessage sendMessage) {
        messageSender.send(map -> {
            ReceiverInfo receiverInfo = sendMessage.getReceiverInfo();
            List<Long> receiverIds = Optional.ofNullable(receiverInfo.getReceiverIds()).orElse(new ArrayList<>());
            // 遍历接收方id
            receiverIds.forEach(receiverId -> {
                ConcurrentHashMap<Integer, Channel> terminalMap = map.get(receiverId);
                send(sendMessage, terminalMap);
            });
        });
    }

    private void send(SendMessage sendMessage, ConcurrentHashMap<Integer, Channel> terminalMap) {
        if (terminalMap == null) {
            // todo 接收不到,  离线消息
            return;
        }
        IMCMD<ChatData> imcmd = MsgConvert.buildIMCMD(sendMessage);

        String imcmdStr = jsonUtils.stringify(imcmd);
        List<Integer> terminalTypes = sendMessage.getReceiverInfo().getReceiveTerminalTypes();

        int count = 0;
        // 遍历在线设备发送信息
        for (Integer terminalType : terminalTypes) {
            Channel channel = terminalMap.get(terminalType);
            if (channel == null) {
                continue;
            }
            try {
                channel.writeAndFlush(new TextWebSocketFrame(imcmdStr));
                count += 1;
                log.info("发送消息: [terminalType: {}, {}]", terminalType, imcmdStr);
            } catch (Exception e) {
                log.error("消息发送异常", e);
            }
        }


        if (count > 0 && sendMessage.getSendType() == 1) {
            // todo
        }
    }


}
