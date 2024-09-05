package com.king.im.listener.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RoomEvent extends ApplicationEvent {

    private final long roomId;

    private final int terminalType;

    private final String notice;

    public RoomEvent(Object source, long roomId, int terminalType, String notice) {
        super(source);
        this.roomId = roomId;
        this.terminalType = terminalType;
        this.notice = notice;
    }
}
