//package com.king.im.server.subscriber;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.king.im.common.enums.MessageStatusEnum;
//import com.king.im.msg.domain.entity.Msg;
//import com.king.im.msg.mapper.MsgMapper;
//import com.king.im.msg.service.MessageService;
//import com.king.im.server.domain.MessageResult;
//import com.king.im.server.protocol.data.ChatData;
//import com.king.im.server.session.MessageCallback;
//import io.netty.channel.Channel;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * 消息状态回执，通知发送者消息发送结果
// */
//@Component
//@Slf4j
//public class MessageResultSubscriber implements Subscriber {
//
//    @Resource
//    private ObjectMapper objectMapper;
//    @Resource
//    private MessageCallback messageCallback;
//    @Resource
//    private MsgMapper msgMapper;
//    @Resource
//    private MessageService messageService;
//
//    @SneakyThrows
//    @Override
//    public void handler(String result) {
//        Map map = objectMapper.readValue(result, Map.class);
//        MessageResult messageResult = objectMapper.convertValue(map, new TypeReference<MessageResult>() {
//        });
//
//        process(messageResult);
//    }
//
//    private void process(MessageResult messageResult) {
//        Long msgId = messageResult.getMsgId();
//        Msg msg = msgMapper.selectById(msgId);
//        if (msg == null) {
//            return;
//        }
//        // 消息发送成功，通知发送者
//        if (MessageStatusEnum.SEND.getType().equals(msg.getStatus())) {
//
//            Long fromUid = msg.getFromUid();
//            messageCallback.handler(map -> {
//                ConcurrentHashMap<Integer, Channel> terminalMap = map.get(fromUid);
//                ChatData chatData = messageService.getMsg(msgId);
//
//                for (Channel channel : terminalMap.values()) {
//                    channel.writeAndFlush(chatData);
//                }
//            });
//        }
//    }
//}
