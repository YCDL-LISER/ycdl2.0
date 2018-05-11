package com.liser.socket.server.handler;

import com.liser.socket.constant.WebSocketConstant;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledDirectByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.nio.ByteBuffer;
import java.util.Date;

public class WebSocketHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 添加通道到通道组
        WebSocketConstant.channelGroup.add(ctx.channel());
    }

    protected void messageReceived(ChannelHandlerContext ctx, Object mesage) throws Exception {
        if(mesage instanceof FullHttpRequest){
            ctx.fireChannelRead(((FullHttpRequest) mesage).retain());
            // 非websocket请求将通道从通道组中移除
            WebSocketConstant.channelGroup.remove(ctx.channel());
            return;
        } else if (!(mesage instanceof TextWebSocketFrame)){
            // 非websocket请求将通道从通道组中移除
            WebSocketConstant.channelGroup.remove(ctx.channel());
            return;
        }

        TextWebSocketFrame mesages = (TextWebSocketFrame)mesage;
        System.out.println("接收到websocket客户端发送信息："+mesages.text());
        String ss = "回复客户端消息";
        byte[] bytes = ss.getBytes();
        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
        ctx.channel().writeAndFlush(new BinaryWebSocketFrame(byteBuf));
        //ctx.channel().writeAndFlush(new TextWebSocketFrame("服务时间："+ new Date()));//c8215874b7f60000-0ee4-00000002-8507d876ad18359b-2d049717
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("websocket移除："+ctx.channel().id().asLongText());
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("WebSocket服务异常发生");
        ctx.close();
    }
}
