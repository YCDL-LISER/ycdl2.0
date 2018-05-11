package com.liser.socket.server;

import com.liser.socket.codec.HolloEncoder;
import com.liser.socket.codec.HolloLastDecoder;
import com.liser.socket.codec.HolloLastEncoder;
import com.liser.socket.codec.HollooDecoder;
import com.liser.socket.server.initializer.SocketServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

// 交给spring管理
@Component
public class SocketServer {

    private static Logger logger = Logger.getLogger(SocketServer.class);

    @Value("${socketServer.port}")
    private int socketPort;

    public void start(WebServer webServer) throws InterruptedException{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap sbs = new ServerBootstrap()
                    .group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class) // 指定所使用的 NIO 传输 Channel
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .localAddress(new InetSocketAddress(socketPort)) // 使用指定的端口设置套接字地址
                    /*.childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 我们在channel链中加入了IdleSateHandler，第一个参数是5，单位是秒，那么这样做的意思就是：
                            // 在服务器端会每隔5秒来检查一下channelRead方法被调用的情况，
                            // 如果在5秒内该链上的channelRead方法都没有被触发，就会调用userEventTriggered方法：
                            ch.pipeline().addLast(new IdleStateHandler(20, 0, 0, TimeUnit.SECONDS));
                            ch.pipeline().addLast("decoder",new HollooDecoder());
                            ch.pipeline().addLast("lastdecoder",new HolloLastDecoder());
                            ch.pipeline().addLast("lastencoder",new HolloLastEncoder());
                            ch.pipeline().addLast("encoder",new HolloEncoder());
                            ch.pipeline().addLast("holloHandle",new SocketServerHandler()); // SocketServerHandler被标注为@Shareable，所以我们可以总是使用同样的实例
                            ch.pipeline().addLast("controlHandle",new SocketControlHandler());
                        };

                    })*/
                    .childHandler(new SocketServerChannelInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // 绑定端口，开始接收进来的连接
            ChannelFuture future = sbs.bind(socketPort).sync(); // 异步地绑定服务器； 调用sync()方法阻塞 等待直到绑定完成
            logger.info("Server start listen at " + socketPort);

            // 启动websocket服务
            webServer.start();

            // 获取Channel的CloseFuture，并且阻塞当前线 程直到它完成
            //future.channel().closeFuture().sync();

        } catch (Exception e) {
            bossGroup.shutdownGracefully(); //关闭 bossGroup， 释放所有的资源
            workerGroup.shutdownGracefully(); //关闭 workerGroup， 释放所有的资源
        } finally {
            bossGroup.shutdownGracefully(); //关闭 bossGroup， 释放所有的资源
            workerGroup.shutdownGracefully(); //关闭 workerGroup， 释放所有的资源
        }
    }

    /*public static void main(String[] args) throws Exception {
        int port = 0;
        if(args.length > 0){
            port = Integer.parseInt(args[0]);
        }else {
            port = 8066;
        }
        SocketServer socketServer = new SocketServer();
        socketServer.start(port);

    }*/


    /**
     * 拆包+粘包解决的方法
     * 1、new LineBasedFrameDecoder(2048) 规定以多少个字节换行
     * 2、new FixedLengthFrameDecoder(23) 规定发送的字节数
     * 3、在req的字符串中增加了“$$__”这样的切割符，然后再Server中照例增加一个DelimiterBasedFrameDecoder，来切割字符串
     *    new DelimiterBasedFrameDecoder(1024,Unpooled.copiedBuffer("$$__".getBytes())
     * 4、CustomMsgDecoder自定义编码器继承LengthFieldBasedFrameDecoder类 解决粘包拆包的问题
     *
     * 一旦一个Channel分配到一个EventLoop，那么这个Channel在其整个的生命周期里只会使用这一个EventLoop，牢记这个特性，
     * 因为这个特性可以使你从担心线程安全和同步你的ChannelHandler这些问题中解脱出来
     *
     */

}
