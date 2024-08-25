package com.king.im.server.strategy;

import com.king.im.common.utils.JSONUtils;
import com.king.im.server.ChannelInfoHolder;
import com.king.im.server.protocol.CMD;
import com.king.im.server.protocol.CMDType;
import com.king.im.server.protocol.cmd.HeartBeatCMD;
import com.king.im.server.session.GlobalSessionManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class HeartbeatMessageStrategy implements MessageStrategy<HeartBeatCMD> {

    @Resource
    private JSONUtils jsonUtils;
    @Resource
    private GlobalSessionManager globalSessionManager;

    // 心条多少次对Redis续命一次
    private static final Integer expirePerTimes = 5;

    @Override
    public void process(CMD cmd, ChannelHandlerContext ctx) {
        // 只有登录用户才能进行心跳
        CMD sendData = new CMD();

        Long uid = ChannelInfoHolder.getUid(ctx.channel());
        Integer terminal = ChannelInfoHolder.getTerminal(ctx.channel());
        if (uid == null) {
            log.error("channelId: {} 未登录认证，无法响应心跳请求", ctx.channel().id());
            sendData.setCmd(CMDType.NON_AUTH);
            ctx.writeAndFlush(new TextWebSocketFrame(jsonUtils.stringify(sendData)));
            ctx.close();
            return;
        }

        // 发送心跳数据
        sendData.setCmd(CMDType.PONG);
        ctx.writeAndFlush(new TextWebSocketFrame(jsonUtils.stringify(sendData)));
        log.debug("channelId: {} 发送心跳", ctx.channel().id());

        Integer heartbeatTime = ChannelInfoHolder.getHeartbeatTime(ctx.channel());
        ChannelInfoHolder.setHeartbeatTime(ctx.channel(), ++heartbeatTime);

        // 进行续期
        if (heartbeatTime % expirePerTimes == 0) {
            globalSessionManager.renewal(uid, terminal);
        }
    }

    @Override
    public int getCmdType() {
        return CMDType.PING;
    }

    @Override
    public HeartBeatCMD getRequest(CMD cmd) {
        return (HeartBeatCMD) cmd.getData();
    }
}
