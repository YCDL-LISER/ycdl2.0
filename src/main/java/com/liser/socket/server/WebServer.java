package com.liser.socket.server;

import com.liser.socket.server.initializer.WebServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import java.net.InetSocketAddress;

@Component
public class WebServer {

    private static Logger logger = Logger.getLogger(WebServer.class);
    private EventLoopGroup webBossGroup;
    private EventLoopGroup webWorkerGroup;

    @Value("${webServer.port}")
    private int webServerPort;

    public void start() throws InterruptedException,ServletException {

        // springmvc配置初始化
        SpringMVCLoader.init();

        webBossGroup = new NioEventLoopGroup();
        webWorkerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap sbs = new ServerBootstrap()
                    .group(webBossGroup, webWorkerGroup)
                    .channel(NioServerSocketChannel.class) // 指定所使用的 NIO 传输 Channel
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .localAddress(new InetSocketAddress(webServerPort)) // 使用指定的端口设置套接字地址
                    .childHandler(new WebServerChannelInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // 绑定端口，开始接收进来的连接, 异步地绑定服务器； 调用sync()方法阻塞 等待直到绑定完成
            ChannelFuture future = sbs.bind(webServerPort).sync();
            logger.info("webSocket start listen at " + webServerPort);

            // 获取Channel的CloseFuture，并且阻塞当前线 程直到它完成
            future.channel().closeFuture().sync();

        } catch (Exception e) {
            webBossGroup.shutdownGracefully(); //关闭 bossGroup， 释放所有的资源
            webWorkerGroup.shutdownGracefully(); //关闭 workerGroup， 释放所有的资源
        } finally {
            webBossGroup.shutdownGracefully(); //关闭 bossGroup， 释放所有的资源
            webWorkerGroup.shutdownGracefully(); //关闭 workerGroup， 释放所有的资源
        }
    }


}
