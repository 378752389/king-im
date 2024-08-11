package com.king.im.listener.event;

import com.king.im.msg.domain.entity.Msg;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MsgEvent extends ApplicationEvent {
    private final Msg msg;

    public MsgEvent(Object source, Msg msg) {
        super(source);
        this.msg = msg;
    }

}
