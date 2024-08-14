package com.king.im.server.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.king.im.server.protocol.CMD;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageStrategyContext {

    @Autowired
    private List<MessageStrategy> strategyList;

    public void process(CMD cmd, ChannelHandlerContext ctx) throws JsonProcessingException {
        for (MessageStrategy messageStrategy : strategyList) {
            if (messageStrategy.getCmdType() == cmd.getCmd()) {
                messageStrategy.process(cmd, ctx);
                break;
            }
        }
    }
}
