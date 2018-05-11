package com.liser.socket.codec;

import com.liser.common.util.ValidateUtil;
import com.liser.socket.bean.HolloDomain;
import com.liser.socket.util.Protocol;
import com.liser.socket.util.ToHexTool;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class HolloEncoder extends MessageToMessageEncoder<HolloDomain> {

    // = LoggerFactory.getLogger(HolloEncoder.class);

    protected void encode(ChannelHandlerContext ctx, HolloDomain holloDomain, List<Object> list) throws Exception {

        // 初始化临时bytebuf大小
        ByteBuf byteBuf = Unpooled.buffer(150);

        if(ValidateUtil.isEmpty(holloDomain)){
            return;
        }

        // 功能id
        String functional_ID = holloDomain.getFunctional_ID();
        byte[] functional_ID_byte = ToHexTool.hexString2Bytes(functional_ID);
        byteBuf.writeBytes(functional_ID_byte);

        /* 消息属性start */
        // 分包标志
        int subpackage_flog = holloDomain.getSubpackage_flog();
        // 消息体加密方式(默认全为0)
        String message_body_encrypt = holloDomain.getMessage_body_encrypt();
        // 消息体长度
        int message_body_length = holloDomain.getMessage_body_length();
        short message_property = (short) (0x01FF & message_body_length);
        byteBuf.writeShort(message_property);
        /* 消息属性end */

        // 设备ID
        int device_ID_length = holloDomain.getDevice_ID().length();
        String device_ID_1 = holloDomain.getDevice_ID().substring(0,1);
        byte[] device_ID_1_byte = device_ID_1.getBytes();
        byteBuf.writeBytes(device_ID_1_byte);
        String device_ID_2 = holloDomain.getDevice_ID().substring(1,device_ID_length);
        byte[] device_ID_2_bytes = ToHexTool.hexString2Bytes(device_ID_2);
        byteBuf.writeBytes(device_ID_2_bytes);

        // 消息流水号
        String message_number = holloDomain.getMessage_number();
        byte[] bytes = ToHexTool.hexString2Bytes(message_number);
        byteBuf.writeBytes(bytes);

        // 消息包封装项strat
        // 消息包总数
        int message_page_total = holloDomain.getMessage_page_total();
        // 消息包序号
        int message_page_number = holloDomain.getMessage_page_number();
        if(subpackage_flog >0){
            short message_page_total_short = (short) message_page_total;
            byteBuf.writeShort(message_page_total_short);
            short message_page_number_short = (short) message_page_number;
            byteBuf.writeShort(message_page_number_short);
        }
        // 消息包封装项end

        //  数据包
        byte[] data_page = holloDomain.getData_page();
        if (!ValidateUtil.isEmpty(data_page)){
            byteBuf.writeBytes(data_page);
        }
        byteBuf.markWriterIndex();

        // 待发送数据
        byte[] await_bytes = new byte[byteBuf.readableBytes()];
        byteBuf.markReaderIndex();
        byteBuf.readBytes(await_bytes);

        // 求出异或值
        byte call_byte_xor = Protocol.checkCode(await_bytes);

        // 最后组装计算出的异或值
        byteBuf.resetWriterIndex();
        byteBuf.resetReaderIndex();
        byteBuf.writeByte(call_byte_xor);

        // 组装后发送出去的数据
        byte[] success_bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(success_bytes);

        // 释放资源
        byteBuf.release();

        // 送往下一个handle进行处理
        list.add(success_bytes);
    }
}
