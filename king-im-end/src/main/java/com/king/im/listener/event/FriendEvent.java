package com.king.im.listener.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class FriendEvent extends ApplicationEvent {

    private final long userId;

    private final long peerId;

    private final int terminalType;

    private final String notice;

    public FriendEvent(Object source, long userId, long peerId, int terminalType, String notice) {
        super(source);
        this.userId = userId;
        this.peerId = peerId;
        this.terminalType = terminalType;
        this.notice = notice;
    }
}
