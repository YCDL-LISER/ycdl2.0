package com.liser.socket.client;

import com.liser.socket.codec.HolloEncoder;
import com.liser.socket.codec.HolloLastDecoder;
import com.liser.socket.codec.HolloLastEncoder;
import com.liser.socket.codec.HollooDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class SocketClient {

    public void connect(String host, int port) throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true) // 禁用nagle算法
                    .remoteAddress(new InetSocketAddress(host, port))
                        .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast("ping", new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS));
                            ch.pipeline().addLast("decoder",new HollooDecoder());
                            ch.pipeline().addLast("lastdecoder",new HolloLastDecoder());
                            ch.pipeline().addLast("lastencoder",new HolloLastEncoder());
                            ch.pipeline().addLast("encoder",new HolloEncoder());
                            p.addLast(new SocketClientHandler());
                        }
                    });

                final ChannelFuture future = bootstrap.connect().sync(); // 连接到远程节点

                /*future.addListener(new ChannelFutureListener() {
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        if (future.isSuccess()){
                            future.channel().writeAndFlush(Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("E7020000764D201608011129AB8D100A080F2E3100900000000001B5F2F106E6772D000000390800000CBD00960000000000000000000000000000000000000000000011FDF9EF80008001000C299C000135020000000000613500008A4300006400A0000003BD0000000000000003A902FD000C299C0001D38C00060007000600070009B9E7"
                                    ,CharsetUtil.UTF_8)));
                        }
                    }
                });*/

                future.channel().closeFuture().sync(); // 阻塞，直到 Channel 关闭

        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8066;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }
        }

        SocketClient socketClient = new SocketClient();
        socketClient.connect("127.0.0.1",port);
    }
}
