package com.king.im.server.strategy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.king.im.common.interceptor.UserInfo;
import com.king.im.common.utils.JSONUtils;
import com.king.im.common.utils.JwtUtils;
import com.king.im.server.ChannelInfoHolder;
import com.king.im.server.ServerBootstrap;
import com.king.im.server.config.IMConfigProperties;
import com.king.im.server.domain.Kickoff;
import com.king.im.server.protocol.CMD;
import com.king.im.server.protocol.CMDType;
import com.king.im.server.protocol.cmd.LoginCMD;
import com.king.im.server.protocol.data.KickoffData;
import com.king.im.server.protocol.data.LoginData;
import com.king.im.server.session.GlobalSessionManager;
import com.king.im.server.session.LocalSessionManager;
import com.king.im.server.subscriber.KickoffSubscriber;
import com.king.im.user.service.LoginService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class LoginMessageStrategy implements MessageStrategy<LoginCMD> {

    @Resource
    private JSONUtils jsonUtils;
    @Resource
    private LoginService loginService;
    @Resource
    private LocalSessionManager localSessionManager;
    @Resource
    private GlobalSessionManager globalSessionManager;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private KickoffSubscriber kickoffSubscriber;

    @Override
    public void process(CMD cmd, ChannelHandlerContext ctx) {
        LoginCMD loginCMD = getRequest(cmd);
        String token = loginCMD.getAccessToken();
        Integer terminal = loginCMD.getTerminal();

        if (loginService.validate(token)) {
            Long uid = loginService.parseToken(token);
            // 校验用户是否已经登录，已经登录进行踢出
            if (globalSessionManager.isOnline(uid, terminal)) {

                Long serverId = globalSessionManager.getServerId(uid, terminal);
                // 之前session和当前登录session在同一台服务器上
                if (serverId != null && serverId.equals(ServerBootstrap.getServerId())) {
                    Channel channel = localSessionManager.getChannel(uid, terminal);
                    if (channel != null) {
                        KickoffData kickoffData = new KickoffData();
                        kickoffData.setReason("用户在其他设备登录，已被强制下线！");
                        kickoffSubscriber.doKickoff(channel, kickoffData);
                    }
                } else {
                    Kickoff kickoff = new Kickoff();
                    kickoff.setUid(uid);
                    kickoff.setTerminal(terminal);
                    kickoff.setReason("用户在其他设备登录，已被强制下线！");
                    redisTemplate.convertAndSend(IMConfigProperties.getKickoffChannel(), kickoff);
                }
            }

            doLogin(ctx, token, uid, terminal);
        }
    }

    private void doLogin(ChannelHandlerContext ctx, String token, Long uid, Integer terminal) {
        CMD sendData = new CMD();
        sendData.setCmd(CMDType.LOGIN);

        LoginData loginData = new LoginData();
        loginData.setAccessToken(token);
        sendData.setData(loginData);

        UserInfo userInfo = JwtUtils.getUserInfoModel(token);

        // todo
        localSessionManager.online(ctx, uid, terminal, userInfo.getUsername());
        // 注册全局session 信息
        globalSessionManager.register(uid, terminal, ServerBootstrap.getServerId());

        ChannelInfoHolder.setUid(ctx.channel(), uid);
        ChannelInfoHolder.setTerminal(ctx.channel(), terminal);
        // todo 设置ip
        String ip = ctx.channel().remoteAddress().toString();
        ChannelInfoHolder.setIp(ctx.channel(), ip);
        ChannelInfoHolder.setHeartbeatTime(ctx.channel(), 0);
        ctx.writeAndFlush(new TextWebSocketFrame(jsonUtils.stringify(sendData)));
        log.info("[uid: {}, username: {}，ip: {} 上线了]", uid, userInfo.getUsername(), ip);
    }

    @Override
    public int getCmdType() {
        return CMDType.LOGIN;
    }

    @Override
    public LoginCMD getRequest(CMD cmd) {
        LoginCMD loginCMD;
        loginCMD = jsonUtils.convert(cmd.getData(), new TypeReference<LoginCMD>() {
        });
        return loginCMD;
    }
}
