package com.king.im.server;

import com.king.im.common.utils.RedisUtils;
import com.king.im.server.subscriber.MessageSubscriber;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import javax.annotation.Resource;
import java.util.List;

@Configuration
@Slf4j
public class ServerBootstrap implements CommandLineRunner {

    @Getter
    private static volatile Long serverId;
    @Resource
    private RedisUtils redisUtils;

    private final List<ServerStarter> serverStarters;
    @Resource
    private RedisConnectionFactory redisConnectionFactory;
    @Resource
    private MessageSubscriber messageSubscriber;

    private static final String MAX_SERVER_ID = "max_server_id";

    public ServerBootstrap(List<ServerStarter> serverStarters) {
        this.serverStarters = serverStarters;
    }

    @Override
    public void run(String... args) throws Exception {
        // 获取serverId
        serverId = redisUtils.incr(MAX_SERVER_ID);

        // 订阅 redis message channel
        subscribeMessageChannel();

        // 启动服务
        for (ServerStarter serverStarter : serverStarters) {
            serverStarter.start();
        }
    }

    private void subscribeMessageChannel() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        ChannelTopic channelTopic = new ChannelTopic("message:" + serverId);

        MessageListenerAdapter adapter = new MessageListenerAdapter(messageSubscriber, "handlerMessage");
        container.addMessageListener(adapter, channelTopic);
        adapter.afterPropertiesSet();
        container.afterPropertiesSet();
        container.start();
        log.info("redis监听容器启动成功！");
    }
}
