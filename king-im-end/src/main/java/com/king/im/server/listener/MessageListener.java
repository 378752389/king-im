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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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

    private static volatile boolean shutdown = false;

    private Thread thread;


    @Override
    public void afterPropertiesSet() {
        Runnable task = () -> {
            log.info("开始监听在线消息");
            while (!shutdown) {
                try {
                    // todo 可以进行改造，做多线程推送
                    ReceiveMessage receiveMessage = messageQueue.take();
                    if (Objects.nonNull(receiveMessage)) {
                        process(receiveMessage);
                    }
                } catch (Exception e) {
                    log.error("消息监听未知异常", e);
                }
            }
        };
        thread = new Thread(task);
        es.submit(thread);
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
        if (es.awaitTermination(5, TimeUnit.SECONDS)) {
            log.info("消息监听器退出成功");
        } else {
            es.shutdownNow();
            log.info("强制终止消息监听");
        }
    }
}
