package com.king.im.server.subscriber;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.king.im.common.enums.ChatTypeEnum;
import com.king.im.common.utils.JSONUtils;
import com.king.im.msg.convert.MsgConvert;
import com.king.im.msg.service.MessageService;
import com.king.im.server.domain.ReceiveMessage;
import com.king.im.server.domain.type.IMUserInfo;
import com.king.im.server.protocol.CMD;
import com.king.im.server.session.MessageSender;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class MessageSubscriber {
    @Resource
    private MessageSender messageSender;
    @Resource
    private JSONUtils jsonUtils;
    @Resource
    private MessageService messageService;
    @Resource
    private ObjectMapper objectMapper;

    @SneakyThrows
    public void handlerMessage(String message) {
        log.debug("接收到 redis channel 订阅消息: {}", message);
        Map map = objectMapper.readValue(message, Map.class);
        ReceiveMessage receiveMessage = objectMapper.convertValue(map, new TypeReference<ReceiveMessage>() {
        });
        process(receiveMessage);
    }

    public void process(ReceiveMessage receiveMessage) {
        messageSender.send(map -> {
            List<IMUserInfo> receiveInfoList = Optional.ofNullable(receiveMessage.getReceivers()).orElse(new ArrayList<>());
            AtomicInteger count = new AtomicInteger();
            // 遍历接收方id
            for (IMUserInfo receiverInfo : receiveInfoList) {
                ConcurrentHashMap<Integer, Channel> terminalMap = map.get(receiverInfo.getId());

                // 用户已离线
                if (CollUtil.isEmpty(terminalMap)) {
                    // todo 离线处理
                    return;
                }

                CMD CMD = MsgConvert.buildIMChatCMD(receiveMessage);
                String imcmdStr = jsonUtils.stringify(CMD);

                Integer terminalType = receiverInfo.getTerminalType();
                Channel channel = terminalMap.get(terminalType);
                if (channel == null) {
                    return;
                }

                try {
                    channel.writeAndFlush(new TextWebSocketFrame(imcmdStr));
                    count.getAndIncrement();
                    log.debug("消息发送成功: [terminalType: {}, {}]", terminalType, imcmdStr);
                } catch (Exception e) {
                    log.error("消息发送失败: ", e);
                }
            }

            // 单聊消息修改消息发送状态 todo 改成异步
            if (count.get() > 0) {
                updateMessageStatus(receiveMessage);
            }

        });


    }

    private void updateMessageStatus(ReceiveMessage receiveMessage) {
        if (ChatTypeEnum.SINGLE.getType().equals(receiveMessage.getChatType())) {
            messageService.updateMsgToSendStatus(receiveMessage.getMsgId());
            log.info("单聊消息发送成功： msgId: {}, content: {}", receiveMessage.getMsgId(), receiveMessage.getContent());
            // todo 通知发送方消息发送成功
        } else if (ChatTypeEnum.GROUP.getType().equals(receiveMessage.getChatType())) {
            // todo
        }
    }
}
