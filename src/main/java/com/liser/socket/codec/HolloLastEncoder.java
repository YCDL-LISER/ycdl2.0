package com.liser.socket.codec;

import com.liser.common.util.ValidateUtil;
import com.liser.socket.util.Protocol;
import com.liser.socket.util.ToHexTool;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.log4j.Logger;

public class HolloLastEncoder extends MessageToByteEncoder<byte[]> {

    Logger logger = Logger.getLogger(HolloLastEncoder.class);


    protected void encode(ChannelHandlerContext ctx, byte[] bytes, ByteBuf byteBuf) throws Exception {

        if(ValidateUtil.isEmpty(bytes)){
            return;
        }

        // 转义
        String await_send_messageStr = ToHexTool.bytes2HexString(bytes);
        await_send_messageStr = Protocol._outDataEscape(await_send_messageStr);
        ByteBuf buf = Unpooled.buffer(bytes.length+2);

        // 添加标识头
        byte[] identifier_start_byte = ToHexTool.hexString2Bytes("E7");
        buf.writeBytes(identifier_start_byte);

        // 转义后待发送数据
        byte[] send_message_bytes = ToHexTool.hexString2Bytes(await_send_messageStr);
        // 添加消息体
        buf.writeBytes(send_message_bytes);

        // 添加标识尾
        byte[] identifier_end_byte = ToHexTool.hexString2Bytes("E7");
        buf.writeBytes(identifier_end_byte);

        byte[] send_bytes = new byte[buf.readableBytes()];
        buf.readBytes(send_bytes);

        // 释放资源
        buf.release();

        // 打印发送给客户端数据16进制字符串
        String send_data = ToHexTool.bytes2HexString(send_bytes);
        logger.info("发送给客户端数据："+send_data);

        // 将消息发送给客户端
        byteBuf.writeBytes(send_bytes);

    }
}
