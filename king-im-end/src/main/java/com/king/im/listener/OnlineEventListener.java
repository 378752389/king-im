package com.king.im.listener;

import com.king.im.listener.event.OnlineEvent;
import com.king.im.msg.service.HistoryMessageCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class OnlineEventListener {

    @Resource
    private HistoryMessageCache historyMessageCache;

    @EventListener(classes = OnlineEvent.class)
    public void sendTo(OnlineEvent event) {

    }
}
