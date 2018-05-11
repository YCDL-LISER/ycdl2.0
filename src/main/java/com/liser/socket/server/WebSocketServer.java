package com.liser.socket.server;

import com.liser.socket.server.handler.HttpHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

import java.net.InetSocketAddress;

// 交给spring管理
@Component
public class WebSocketServer {

    private Logger logger = Logger.getLogger(WebSocketServer.class.getName());
    private EventLoopGroup bossGroup2;
    private EventLoopGroup workerGroup2;

    public void start(final int port) throws InterruptedException {

        bossGroup2 = new NioEventLoopGroup();
        workerGroup2 = new NioEventLoopGroup();

        try {
            ServerBootstrap sbs = new ServerBootstrap()
                    .group(bossGroup2, workerGroup2)
                    .channel(NioServerSocketChannel.class) // 指定所使用的 NIO 传输 Channel
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .localAddress(new InetSocketAddress(port)) // 使用指定的端口设置套接字地址
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // websocket协议本身是基于http协议的，所以这边也要使用http解编码器
                            ch.pipeline().addLast(new HttpServerCodec());
                            // netty是基于分段请求的，HttpObjectAggregator的作用是将请求分段再聚合,参数是聚合字节的最大长度
                            ch.pipeline().addLast(new HttpObjectAggregator(64 * 1024));
                            // 用来向客户端发送HTML5文件，它主要用于支持浏览器和服务端进行WebSocket通信
                            ch.pipeline().addLast(new ChunkedWriteHandler());
                            ch.pipeline().addLast(new HttpHandler(SpringMVCLoader.getDispatcherServlet()));
                            ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ycdl/websocket"));
                            ch.pipeline().addLast(new WebSocketServerHandler());
                        }

                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // 绑定端口，开始接收进来的连接
            ChannelFuture future = sbs.bind(port).sync(); // 异步地绑定服务器； 调用sync()方法阻塞 等待直到绑定完成
            logger.info("webSocket start listen at " + port);
            future.channel().closeFuture().sync(); // 获取Channel的CloseFuture，并且阻塞当前线 程直到它完成

        } catch (Exception e) {
            bossGroup2.shutdownGracefully(); //关闭 bossGroup， 释放所有的资源
            workerGroup2.shutdownGracefully(); //关闭 workerGroup， 释放所有的资源
        } finally {
            bossGroup2.shutdownGracefully(); //关闭 bossGroup， 释放所有的资源
            workerGroup2.shutdownGracefully(); //关闭 workerGroup， 释放所有的资源
        }
    }

    public void close(){
        bossGroup2.shutdownGracefully();
        workerGroup2.shutdownGracefully();
    }

    public static void main(String[] args) throws InterruptedException {
        WebSocketServer webSocketServer = new WebSocketServer();
        webSocketServer.start(8090);
    }
}

