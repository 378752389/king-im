package com.king.im.ws;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.king.im.sender.protocol.IMCMD;
import com.king.im.sender.protocol.IMCMDType;
import com.king.im.sender.protocol.cmd.LoginCMD;
import com.king.im.sender.protocol.data.LoginData;
import com.king.im.user.service.LoginService;
import com.king.im.ws.session.GlobalSessionManager;
import com.king.im.ws.session.LocalSessionManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicLong;

@Component
@ChannelHandler.Sharable
@Slf4j
public class WebSocketHandler extends ChannelInboundHandlerAdapter {
    @Resource
    private LocalSessionManager localSessionManager;
    @Resource
    private GlobalSessionManager globalSessionManager;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private LoginService loginService;

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
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        TextWebSocketFrame frame = (TextWebSocketFrame) msg;
        IMCMD recData = objectMapper.readValue(frame.text(), IMCMD.class);

        IMCMD sendData = new IMCMD();
        switch (recData.getCmd()) {
            case IMCMDType.PING: {
                // 只有登录用户才能进行心跳
                Long uid = RequestInfoHolder.getUid(ctx.channel());
                if (uid == null) {
                    sendData.setCmd(IMCMDType.NON_AUTH);
                    ctx.writeAndFlush(new TextWebSocketFrame(objectMapper.writeValueAsString(sendData)));
                    break;
                }

                sendData.setCmd(IMCMDType.PONG);
                ctx.writeAndFlush(new TextWebSocketFrame(objectMapper.writeValueAsString(sendData)));
                break;
            }
            case IMCMDType.LOGIN: {
                Object data = recData.getData();
                LoginCMD loginCMD;
                try {
                    loginCMD = objectMapper.convertValue(data, new TypeReference<LoginCMD>() {
                    });
                } catch (IllegalArgumentException e) {
                    log.error("登录命令解析失败， 数据内容：{}", data);
                    break;
                }

                String token = loginCMD.getAccessToken();
                if (loginService.validate(token)) {
                    Long uid = loginService.parseToken(token);

                    sendData.setCmd(IMCMDType.LOGIN);
                    LoginData loginData = new LoginData();
                    loginData.setAccessToken(token);
                    sendData.setData(loginData);

                    localSessionManager.online(ctx, uid, 1);

                    RequestInfoHolder.setUid(ctx.channel(), uid);
                    ctx.writeAndFlush(new TextWebSocketFrame(objectMapper.writeValueAsString(sendData)));
                    log.info("执行登录命令： uid: {}", uid);
                }
                break;
            }
            case IMCMDType.LOGOUT: {
                Long uid = RequestInfoHolder.getUid(ctx.channel());
                localSessionManager.offline(ctx, uid, 1);

                sendData.setCmd(IMCMDType.LOGOUT);
                sendData.setData("");
                ctx.writeAndFlush(new TextWebSocketFrame(objectMapper.writeValueAsString(sendData)));
                log.info("执行退出登录命令： uid: {}", uid);
                break;
            }
            default:
                break;
        }

        ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;

            if (e.state().equals(IdleState.READER_IDLE)) {
//                ctx.channel().close();
                log.info("读超时");
//                System.out.println("读超时");
            } else if (e.state().equals(IdleState.WRITER_IDLE)) {
//                System.out.println("写超时");
            } else if (e.state().equals(IdleState.ALL_IDLE)) {
//                System.out.println("读写超时");
            }
        }
        ctx.fireUserEventTriggered(evt);
    }
}
