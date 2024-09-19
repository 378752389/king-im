package com.king.im.server.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.king.im.server.ChannelInfoHolder;
import com.king.im.server.domain.Kickoff;
import com.king.im.server.protocol.CMD;
import com.king.im.server.protocol.CMDType;
import com.king.im.server.protocol.data.KickoffData;
import com.king.im.server.session.LocalSessionManager;
import com.king.im.server.session.MessageCallback;
import io.netty.channel.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class KickoffSubscriber implements Subscriber {

    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private MessageCallback messageCallback;
    @Resource
    private LocalSessionManager localSessionManager;

    @Override
    @SneakyThrows
    public void handler(String message) {
        Map map = objectMapper.readValue(message, Map.class);
        Kickoff kickoff = objectMapper.convertValue(map, Kickoff.class);
        Long uid = kickoff.getUid();
        Integer terminal = kickoff.getTerminal();

        KickoffData kickoffData = new KickoffData();
        kickoffData.setReason(kickoff.getReason());

        messageCallback.handler(m -> {
            Optional.of(m)
                    .map(s -> s.get(uid))
                    .map(terminalMap -> terminalMap.get(terminal))
                    .ifPresent(channel -> doKickoff(channel, kickoffData));
        });
    }

    /**
     * 强制下线只修改 localSession
     * @param channel
     */
    @SneakyThrows
    public void doKickoff(Channel channel, KickoffData kickoffData) {
        Long uid = ChannelInfoHolder.getUid(channel);
        Integer terminal = ChannelInfoHolder.getTerminal(channel);

        CMD cmd = new CMD();
        cmd.setCmd(CMDType.KICKOFF);
        cmd.setData(kickoffData);

        channel.writeAndFlush(cmd);
        // todo 下线命令
        localSessionManager.offline(channel, uid, terminal);
        log.info("uid: {}, terminal: {} 被强制下限", uid, terminal);
    }
}
