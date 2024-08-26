package com.king.im.listener;

import com.king.im.listener.event.OnlineEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OnlineEventListener {

    @EventListener(classes = OnlineEvent.class)
    public void sendTo(OnlineEvent event) {

    }
}
