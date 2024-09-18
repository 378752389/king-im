package com.king.im.server.ws;

import com.king.im.server.IMServerHandler;
import com.king.im.server.ServerStarter;
import com.king.im.server.ws.codec.IMWebSocketDecoder;
import com.king.im.server.ws.codec.IMWebSocketEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class WSServerStarter implements ServerStarter {

    @Resource
    private IMServerHandler IMServerHandler;

    @Override
    public void start() {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup(10);

        ServerBootstrap bootstrap = new ServerBootstrap();
        ChannelFuture future = null;

        try {
            future = bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new IdleStateHandler(12, 0, 0))
                                    .addLast(new HttpServerCodec())
                                    .addLast(new HttpObjectAggregator(8192))
                                    .addLast(new ChunkedWriteHandler())
                                    .addLast(new HttpHeadHandler())
                                    .addLast(new WebSocketServerProtocolHandler("/"))
                                    .addLast(new IMWebSocketEncoder())
                                    .addLast(new IMWebSocketDecoder())
                                    .addLast(IMServerHandler);
                        }
                    })
                    .bind(8849)
                    .sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        log.info("ws服务启动成功！");
    }

    @Override
    public void stop() {

    }
}
