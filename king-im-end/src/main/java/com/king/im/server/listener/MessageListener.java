package com.king.im.server.listener;

import cn.hutool.core.collection.CollUtil;
import com.king.im.common.enums.ChatTypeEnum;
import com.king.im.common.utils.JSONUtils;
import com.king.im.msg.convert.MsgConvert;
import com.king.im.msg.service.MessageService;
import com.king.im.server.domain.ReceiveMessage;
import com.king.im.server.domain.type.IMUserInfo;
import com.king.im.server.protocol.CMD;
import com.king.im.server.queue.MessageQueue;
import com.king.im.server.session.MessageSender;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class MessageListener implements InitializingBean, DisposableBean {

    @Resource
    private MessageQueue messageQueue;
    @Resource
    private MessageSender messageSender;
    @Resource
    private JSONUtils jsonUtils;
    @Resource
    private MessageService messageService;

    private static final ExecutorService es = Executors.newSingleThreadExecutor();


    @Override
    public void afterPropertiesSet() {
        es.submit(new Runnable() {
            @Override
            @SneakyThrows
            public void run() {
                try {
                    ReceiveMessage receiveMessage = messageQueue.take();
                    while (Objects.nonNull(receiveMessage)) {
                        // todo 可以进行改造，做多线程推送
                        process(receiveMessage);
                        receiveMessage = messageQueue.take();
                    }
                } catch (Exception e) {
                    log.error("消息监听未知异常", e);
                }

                // behavior
                if (!es.isShutdown()) {
                    Thread.sleep(200);
                    es.execute(this);
                }
            }
        });
    }

    public void process(ReceiveMessage receiveMessage) {
        messageSender.send(map -> {
            List<IMUserInfo> receiveInfoList = Optional.ofNullable(receiveMessage.getReceivers()).orElse(new ArrayList<>());
            AtomicInteger count = new AtomicInteger();
            // 遍历接收方id
            receiveInfoList.forEach(receiverInfo -> {
                ConcurrentHashMap<Integer, Channel> terminalMap = map.get(receiverInfo.getId());

                // 用户已离线
                if (CollUtil.isEmpty(terminalMap)) {
                    // todo 离线处理
                    return;
                }

                CMD CMD = MsgConvert.buildIMCMD(receiveMessage);
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

            });

            // 单聊消息修改消息发送状态
            if (count.get() > 0) {
                if (ChatTypeEnum.SINGLE.getType().equals(receiveMessage.getChatType())) {
                    messageService.updateMsgToSendStatus(receiveMessage.getMsgId());
                    log.info("单聊消息发送成功： msgId: {}, content: {}", receiveMessage.getMsgId(), receiveMessage.getContent());
                } else if (ChatTypeEnum.GROUP.getType().equals(receiveMessage.getChatType())) {
                    // todo
                }
            }

        });


    }

    @Override
    public void destroy() throws Exception {
        es.shutdown();
        log.info("消息监听器停止接受新消息");
    }
}
