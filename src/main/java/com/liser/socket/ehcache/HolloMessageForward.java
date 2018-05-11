package com.liser.socket.ehcache;

import com.alibaba.fastjson.JSON;
import com.liser.common.util.ValidateUtil;
import com.liser.socket.bean.HolloDomain;
import com.liser.socket.constant.WebSocketConstant;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.FutureListener;

import java.util.Map;

public class HolloMessageForward {

    /**
     * 下发平台控制指令给终端
     * @param device_ID
     * @param holloDomain
     * @return
     */
    public static boolean sendData2Endpoint(String device_ID, HolloDomain holloDomain){
        // 通过设备ID获取socket通道ID
        String channel_ID = (String) HolloCacheFactory.getSocketCache(device_ID);
        ChannelHandlerContext ctx = (ChannelHandlerContext) HolloCacheFactory.getSocketCache(channel_ID);

        // 发送数据给终端
        if (ValidateUtil.isEmpty(ctx) || !ctx.channel().isActive()){
            return false;
        }
        ChannelFuture future = ctx.writeAndFlush(holloDomain);
        future.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture channelFuture) throws Exception {

            }
        });
        return true;
    }

    /**
     * 上行终端控制应答结果给平台
     * @param device_ID
     * @param appMap
     */
    public static void sendData2Platform(String device_ID, Map appMap){
        // 通过设备ID获取webSocket通道ID
        String channel_ID = (String) HolloCacheFactory.getWebSocketCache(device_ID);
        ChannelHandlerContext ctx = (ChannelHandlerContext) HolloCacheFactory.getWebSocketCache(channel_ID);

        // 待发送数据转换为json字符串
        String sendMsg = JSON.toJSONString(appMap);
        // 发送数据给平台
        ctx.channel().writeAndFlush(new TextWebSocketFrame(sendMsg));

    }

    public static void sendData2SynergyPlatform(Object object){
        if (object instanceof HolloDomain)
        WebSocketConstant.channelGroup.writeAndFlush(new BinaryWebSocketFrame());

    }
}
