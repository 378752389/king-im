package com.king.im.server.subscriber;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.king.im.common.domain.RecMessage;
import com.king.im.common.domain.type.IMUserInfo;
import com.king.im.common.enums.ChatTypeEnum;
import com.king.im.common.enums.MessageStatusEnum;
import com.king.im.common.utils.JSONUtils;
import com.king.im.msg.convert.MsgConvert;
import com.king.im.msg.service.MessageService;
import com.king.im.server.protocol.CMD;
import com.king.im.server.session.MessageCallback;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class MessageSubscriber implements Subscriber {
    @Resource
    private MessageCallback messageCallback;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private MessageService messageService;

    @SneakyThrows
    @Override
    public void handler(String message) {
        log.debug("接收到 redis channel 订阅消息: {}", message);
        Map map = objectMapper.readValue(message, Map.class);
        RecMessage recMessage = objectMapper.convertValue(map, new TypeReference<RecMessage>() {
        });
        process(recMessage);
    }

    public void process(RecMessage recMessage) {
        messageCallback.handler(map -> {
            IMUserInfo receiver = recMessage.getReceiver();
            Optional.of(map)
                    .map(m -> m.get(receiver.getId()))
                    .map(t -> t.get(receiver.getTerminalType()))
                    .ifPresent(channel -> {
                        CMD cmd = MsgConvert.buildIMChatCMD(recMessage);
                        try {
                            channel.writeAndFlush(cmd);
                            setResult(recMessage, MessageStatusEnum.SEND);
                        } catch (Exception e) {
                            log.error("消息发送失败", e);
                            setResult(recMessage, MessageStatusEnum.WAITING_SEND);
                        }
                    });
        });
    }

    public void setResult(RecMessage recMessage, MessageStatusEnum statusEnum) {
        if (ChatTypeEnum.SINGLE.getType().equals(recMessage.getChatType())) {
            if (MessageStatusEnum.SEND.equals(statusEnum)) {
                messageService.updateMsgToSendStatus(recMessage.getMsgId());
                log.info("单聊消息发送成功： msgId: {}, content: {}", recMessage.getMsgId(), recMessage.getContent());
            }
            // todo 通知发送方消息发送成功
        } else if (ChatTypeEnum.GROUP.getType().equals(recMessage.getChatType())) {
            // todo
        }
    }

//    public void process(ReceiveMessage receiveMessage) {
//        messageCallback.handler(map -> {
//            List<IMUserInfo> receiveInfoList = Optional.ofNullable(receiveMessage.getReceivers()).orElse(new ArrayList<>());
//            AtomicInteger count = new AtomicInteger();
//            // 遍历接收方id
//            for (IMUserInfo receiverInfo : receiveInfoList) {
//                ConcurrentHashMap<Integer, Channel> terminalMap = map.get(receiverInfo.getId());
//
//                // 用户已离线
//                if (CollUtil.isEmpty(terminalMap)) {
//                    // todo 离线处理
//                    return;
//                }
//
//                CMD CMD = MsgConvert.buildIMChatCMD(receiveMessage);
//                String imcmdStr = jsonUtils.stringify(CMD);
//
//                Integer terminalType = receiverInfo.getTerminalType();
//                Channel channel = terminalMap.get(terminalType);
//                if (channel == null) {
//                    return;
//                }
//
//                try {
//                    channel.writeAndFlush(new TextWebSocketFrame(imcmdStr));
//                    count.getAndIncrement();
//                    log.debug("消息发送成功: [terminalType: {}, {}]", terminalType, imcmdStr);
//                } catch (Exception e) {
//                    log.error("消息发送失败: ", e);
//                }
//            }
//
//            // 单聊消息修改消息发送状态 todo 改成异步
//            if (count.get() > 0) {
//                updateMessageStatus(receiveMessage);
//            }
//
//        });
//
//
//    }

//    private void updateMessageStatus(ReceiveMessage receiveMessage) {
//        if (ChatTypeEnum.SINGLE.getType().equals(receiveMessage.getChatType())) {
//            messageService.updateMsgToSendStatus(receiveMessage.getMsgId());
//            log.info("单聊消息发送成功： msgId: {}, content: {}", receiveMessage.getMsgId(), receiveMessage.getContent());
//
////            MessageResult messageResult = new MessageResult();
////            messageResult.setMsgId(receiveMessage.getMsgId());
////            messageResult.setStatus(MessageStatusEnum.SEND.getType());
////            redisTemplate.convertAndSend("messageResult:" + ServerBootstrap.getServerId(), messageResult);
//            // todo 通知发送方消息发送成功
//        } else if (ChatTypeEnum.GROUP.getType().equals(receiveMessage.getChatType())) {
//            // todo
//        }
//    }
}
