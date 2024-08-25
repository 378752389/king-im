package com.king.im.server.strategy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.king.im.common.utils.JSONUtils;
import com.king.im.server.ChannelInfoHolder;
import com.king.im.server.ServerBootstrap;
import com.king.im.server.protocol.CMD;
import com.king.im.server.protocol.CMDType;
import com.king.im.server.protocol.cmd.LoginCMD;
import com.king.im.server.protocol.data.LoginData;
import com.king.im.server.session.GlobalSessionManager;
import com.king.im.server.session.LocalSessionManager;
import com.king.im.user.service.LoginService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class LoginMessageStrategy implements MessageStrategy<LoginCMD>{

    @Resource
    private JSONUtils jsonUtils;
    @Resource
    private LoginService loginService;
    @Resource
    private LocalSessionManager localSessionManager;
    @Resource
    private GlobalSessionManager globalSessionManager;

    @Override
    public void process(CMD cmd, ChannelHandlerContext ctx) {
        LoginCMD loginCMD = getRequest(cmd);
        String token = loginCMD.getAccessToken();
        Integer terminal = loginCMD.getTerminal();

        if (loginService.validate(token)) {
            Long uid = loginService.parseToken(token);

            CMD sendData = new CMD();
            sendData.setCmd(CMDType.LOGIN);

            LoginData loginData = new LoginData();
            loginData.setAccessToken(token);
            sendData.setData(loginData);

            localSessionManager.online(ctx, uid, 1);
            // 注册全局session 信息
            globalSessionManager.register(uid, terminal, ServerBootstrap.getServerId());

            ChannelInfoHolder.setUid(ctx.channel(), uid);
            ChannelInfoHolder.setHeartbeatTime(ctx.channel(), 0);
            ctx.writeAndFlush(new TextWebSocketFrame(jsonUtils.stringify(sendData)));
            log.info("执行登录命令： uid: {}", uid);
        }
    }

    @Override
    public int getCmdType() {
        return CMDType.LOGIN;
    }

    @Override
    public LoginCMD getRequest(CMD cmd) {
        LoginCMD loginCMD;
        loginCMD = jsonUtils.convert(cmd.getData(), new TypeReference<LoginCMD>() {});
        return loginCMD;
    }
}
