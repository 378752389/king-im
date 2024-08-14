package com.king.im.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.king.im.server.protocol.CMD;
import com.king.im.server.strategy.MessageStrategyContext;
import com.king.im.user.service.LoginService;
import com.king.im.server.session.GlobalSessionManager;
import com.king.im.server.session.LocalSessionManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicLong;

@Component
@ChannelHandler.Sharable
@Slf4j
public class IMServerHandler extends SimpleChannelInboundHandler<CMD> {
    @Resource
    private LocalSessionManager localSessionManager;
    @Resource
    private GlobalSessionManager globalSessionManager;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private LoginService loginService;
    @Resource
    private MessageStrategyContext messageStrategyContext;

    private static final AtomicLong count = new AtomicLong(0);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        localSessionManager.disconnect(ctx);
        ctx.close();
        log.info("设备： {} 下线", ctx.channel().id());
        ctx.fireChannelInactive();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        localSessionManager.connect(ctx);
        log.info("设备： {} 上线", ctx.channel().id());
        ctx.fireChannelActive();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CMD CMD) throws Exception {
        messageStrategyContext.process(CMD, ctx);
        ctx.fireChannelRead(CMD);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("未知错误：", cause);
        ctx.fireExceptionCaught(cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state().equals(IdleState.READER_IDLE)) {
                log.info("读超时");
            }
        }

        ctx.fireUserEventTriggered(evt);
    }
}