package com.liser.socket.server;

import com.alibaba.fastjson.JSON;
import com.liser.common.util.ValidateUtil;
import com.liser.socket.bean.HolloDomain;
import com.liser.socket.ehcache.HolloCacheFactory;
import com.liser.socket.ehcache.HolloMessageForward;
import com.liser.socket.util.ToHexTool;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Date;


//处理文本协议数据，处理TextWebSocketFrame类型的数据，websocket专门处理文本的frame就是TextWebSocketFrame
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //每个channel都有一个唯一的id值
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //打印出channel唯一值，asLongText方法是channel的id的全名
        String id = ctx.channel().id().asLongText();
        System.out.println("handlerAdded："+id);

        // 加入缓存
        String channel_ID = ctx.channel().id().asLongText();
        HolloCacheFactory.putWebSocketCache(channel_ID, ctx);

        /*Cache ecache = (Cache)ServiceLocator.getService("ehCache");
        Element element = new Element("user",ctx);
        Element element2 = new Element("password",123456);
        ecache.put(element);
        ecache.put(element2);*/

        // WebSocketConstant.pushCtxMap.put(id, ctx);

    }

    protected void messageReceived(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("收到消息："+msg.text());

        HolloDomain holloDomain = JSON.parseObject(msg.text(),HolloDomain.class);
        if(!ValidateUtil.isEmpty(holloDomain.getData_page_hex())){
            holloDomain.setData_page(ToHexTool.hexString2Bytes(holloDomain.getData_page_hex()));
        }

/*        String url = (String) ctx.attr(AttributeKey.valueOf("type")).get();

        Map dataMap = JSON.parseObject(msg.text(), Map.class);

        String functional_ID = (String) dataMap.get("functionID");
        Integer message_body_length = (Integer) dataMap.get("messageBodyLength");
        String device_ID = (String) dataMap.get("deviceID");
        String command = (String) dataMap.get("command");
        String content = (String) dataMap.get("content");

        // 加入缓存
        HolloCacheUtil.putWebSocketCache(device_ID, ctx.channel().id().asLongText());

        // 创建对象准备应答
        HolloDomain holloDomain_heart_up_head = new HolloDomain();
        // 功能ID
        holloDomain_heart_up_head.setFunctional_ID(functional_ID);
        // 消息属性start：平台通用应答默认为5个字节
        holloDomain_heart_up_head.setSubpackage_flog(0); // 不分包
        holloDomain_heart_up_head.setMessage_body_length(message_body_length); // 消息体长度
        // 消息属性end
        // 设备ID
        holloDomain_heart_up_head.setDevice_ID(device_ID);
        // 消息流水号
        holloDomain_heart_up_head.setMessage_number("0001");
        // 消息体
        byte[] general_response_bytes={};
        String hexs = command + content;
        if(!ValidateUtil.isEmpty(hexs)){
            general_response_bytes = ToHexTool.hexString2Bytes(hexs);
        }
        holloDomain_heart_up_head.setData_page(general_response_bytes);*/

        // 发送控制命令给终端
        boolean success = HolloMessageForward.sendData2Endpoint(holloDomain.getDevice_ID(), holloDomain);
        if (success){
            ctx.channel().writeAndFlush(new TextWebSocketFrame("设置成功!"));
        }else {
            ctx.channel().writeAndFlush(new TextWebSocketFrame("终端已经下线!"));
        }

        /**
         * writeAndFlush接收的参数类型是Object类型，但是一般我们都是要传入管道中传输数据的类型，比如我们当前的demo
         * 传输的就是TextWebSocketFrame类型的数据
         */
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务时间："+ new Date()));//c8215874b7f60000-0ee4-00000002-8507d876ad18359b-2d049717
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        String id = ctx.channel().id().asLongText();
        System.out.println("handlerRemoved：" + id);

        // 移除缓存
        String channel_ID = ctx.channel().id().asLongText();
        HolloCacheFactory.removeWebSocketCache(channel_ID);

        /*for(String key : WebSocketConstant.pushCtxMap.keySet()){
            if(ctx.equals(WebSocketConstant.pushCtxMap.get(key))){
                //从连接池内剔除
                System.out.println(WebSocketConstant.pushCtxMap.size());
                System.out.println("剔除"+key);
                WebSocketConstant.pushCtxMap.remove(key);
                System.out.println(WebSocketConstant.pushCtxMap.size());
            }
        }*/
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生");
        ctx.close();
    }
}

