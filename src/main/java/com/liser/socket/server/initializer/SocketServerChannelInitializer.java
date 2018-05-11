package com.liser.socket.server.initializer;

import com.liser.socket.codec.HolloEncoder;
import com.liser.socket.codec.HolloLastDecoder;
import com.liser.socket.codec.HolloLastEncoder;
import com.liser.socket.codec.HollooDecoder;
import com.liser.socket.server.adapter.SocketControlHandler;
import com.liser.socket.server.adapter.SocketServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class SocketServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    protected void initChannel(SocketChannel channel) throws Exception {
        // 我们在channel链中加入了IdleSateHandler，第一个参数是20，单位是秒，那么这样做的意思就是：
        // 在服务器端会每隔20秒来检查一下channelRead方法被调用的情况，
        // 如果在20秒内该链上的channelRead方法都没有被触发，就会调用userEventTriggered方法：
        channel.pipeline().addLast(new IdleStateHandler(20, 0, 0, TimeUnit.SECONDS));
        channel.pipeline().addLast("decoder",new HollooDecoder());
        channel.pipeline().addLast("lastdecoder",new HolloLastDecoder());
        channel.pipeline().addLast("lastencoder",new HolloLastEncoder());
        channel.pipeline().addLast("encoder",new HolloEncoder());
        // SocketServerHandler被标注为@Shareable，所以我们可以总是使用同样的实例
        channel.pipeline().addLast("holloHandle",new SocketServerHandler());
        channel.pipeline().addLast("controlHandle",new SocketControlHandler());
    }
}
