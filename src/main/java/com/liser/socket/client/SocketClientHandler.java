package com.liser.socket.client;

import com.liser.socket.util.ToHexTool;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

import java.util.Date;

@ChannelHandler.Sharable // 标记该类的实例可以被 多个 Channel 共享
public class SocketClientHandler extends ChannelInboundHandlerAdapter {

    private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("保存连接"
            ,CharsetUtil.UTF_8));
    private static final int TRY_TIMES = 3; // 发送次数
    private int currentTime = 0;

    private byte[] req; //
    private int count;

    public SocketClientHandler(){

        this.req = ("In this chapter you general, we recommend Java Concurrency in Practice by Brian Goetz. His book w"
                + "ill give We’ve reached an exciting point—in the next chapter we’ll discuss bootstrapping, the process "
                + "of configuring and connecting all of Netty’s components to bring your learned about threading models in ge"
                + "neral and Netty’s threading model in particular, whose performance and consistency advantages we discuss"
                + "ed in detail In this chapter you general, we recommend Java Concurrency in Practice by Brian Goetz. Hi"
                + "s book will give We’ve reached an exciting point—in the next chapter we’ll discuss bootstrapping, the"
                + " process of configuring and connecting all of Netty’s components to bring your learned about threading "
                + "models in general and Netty’s threading model in particular, whose performance and consistency advantag"
                + "es we discussed in detailIn this chapter you general, we recommend Java Concurrency in Practice by Bri"
                + "an Goetz. His book will give We’ve reached an exciting point—in the next chapter;the counter is: 1 2222"
                + "sdsa ddasd asdsadas dsadasdas" + System.getProperty("line.separator")).getBytes();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("激活时间是："+new Date());
        System.out.println("SocketClientHandler channelActive");

       // ByteBuf byteBuf = Unpooled.
        /*String ss = "E7020000764D201608011129AB8D100A080F2E3100900000000001B5F2F106E6772D000000390800000CBD00960000000000000000000000000000000000000000000011FDF9EF80008001000C299C000135020000000000613500008A4300006400A0000003BD0000000000000003A902FD000C299C0001D38C00060007000600070009B9E7";
        for(int i=0; i<10; i++  ){
            ctx.writeAndFlush(i+ss);
            System.out.println("发送次数："+(i+1));
        }*/
//        byte[] bytes = {(byte)0xE7,(byte)0x20,(byte)0x00,(byte)0x00,(byte)0x76,(byte)0x4D};
        String ss = "E7010200805412345678901101D7484C543430305F56323032333431323031382D30342D31390030003E33374718323538393030543132333435363738393031313836373138343033323730343437343436303031353632363238303938363839383630313136383531313136343232343638F00C000000000000000000000000000000000000000000000000001BE7";
        byte[] bytes = ToHexTool.hexString2Bytes(ss);
        ctx.writeAndFlush(bytes);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("停止时间是："+new Date());
        System.out.println("SocketClientHandler channelInactive");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("循环触发时间："+new Date());
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;

            // 向服务器发送数据
            if (event.state() == IdleState.WRITER_IDLE) {
                if(currentTime <= TRY_TIMES){
                    System.out.println("currentTime:"+currentTime);
                    currentTime++;
                    //ctx.writeAndFlush("保持连接");
                }
            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务器返回数据：" + msg+ "；第" + (++count));

        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        System.out.println("服务端返回数据：");
        for(byte b : bytes) {
            System.out.print(b+" ");
        }
        System.out.println("");

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将未决消息冲刷到 远程节点，并且关 闭该 Channel
        // ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

}
