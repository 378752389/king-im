package com.king.im.listener.event;

import org.springframework.context.ApplicationEvent;

public class OnlineEvent extends ApplicationEvent {

    public OnlineEvent(Object source) {
        super(source);
    }
}
