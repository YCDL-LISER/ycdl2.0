package com.liser.socket.codec;

import com.liser.common.exception.AppException;
import com.liser.common.util.ValidateUtil;
import com.liser.socket.bean.HolloDomain;
import com.liser.socket.util.ToHexTool;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class HolloLastDecoder extends MessageToMessageDecoder {

    public HolloLastDecoder() {

    }

    protected void decode(ChannelHandlerContext ctx, Object object, List list) throws Exception {

        HolloDomain holloDomain = new HolloDomain();

        byte[] message_bytes = {};

        try {
            message_bytes = (byte[]) object;
        } catch (Exception e){
            throw new AppException("客户端消息转换为bytr[]数组失败!");
        }

        if (ValidateUtil.isEmpty(message_bytes)) {
            return;

        } else if (message_bytes.length == 14) {
            // 应答结果(应答失败的情况)

            // 将数组数据暂存到bytebuf中
            int message_useful = message_bytes.length;
            ByteBuf data_buf = Unpooled.buffer(message_useful);
            data_buf.writeBytes(message_bytes);

            // 功能ID
            byte[] Functional_ID_bytes = new byte[2];
            data_buf.readBytes(Functional_ID_bytes);
            String Functional_ID = ToHexTool.bytes2HexString(Functional_ID_bytes);
            holloDomain.setFunctional_ID(Functional_ID);

            // 消息属性strat
            byte[] message_property_bytes_success = new byte[2];
            data_buf.readBytes(message_property_bytes_success);
            // 分包标识
            int subpackage_flog_success = message_property_bytes_success[0] & 0x20;
            holloDomain.setSubpackage_flog(subpackage_flog_success);
            // 消息体加密方式
            int message_body_encrypt = message_property_bytes_success[0] & 0x1C;
            holloDomain.setMessage_body_encrypt(Integer.toBinaryString(message_body_encrypt));
            // 消息体长度
            int message_type_1 = message_property_bytes_success[0] & 0x03;
            int message_type_2 = message_property_bytes_success[1] & 0xFF;
            int message_body_length = message_type_1 + message_type_2;
            holloDomain.setMessage_body_length(message_body_length);
            // 消息属性end

            // 设备ID
            int device_ID_1 = data_buf.readByte();
            char device_ID_ascii = (char) device_ID_1;
            String device_ID_2 = ToHexTool.HexString(data_buf.readUnsignedInt());
            String device_ID_3 = ToHexTool.HexString(data_buf.readUnsignedShort());
            String device_ID = "" + device_ID_ascii + device_ID_2 + device_ID_3;
            holloDomain.setDevice_ID(device_ID);

            // 消息流水号
            byte[] message_number_bytes = new byte[2];
            data_buf.readBytes(message_number_bytes);
            String message_number_str = ToHexTool.bytes2HexString(message_number_bytes);
            holloDomain.setMessage_number(message_number_str);

            // 应答结果
            byte[] success_byte = new byte[1];
            data_buf.readBytes(success_byte);
            String success = ToHexTool.bytes2HexString(success_byte);
            holloDomain.setSuccess(success);

            // 释放资源
            data_buf.release();

            list.add(holloDomain);

        } else {
            // 正确的数据

            int message_useful = message_bytes.length;
            ByteBuf data_buf = Unpooled.buffer(message_useful);
            data_buf.writeBytes(message_bytes);

            // 功能ID
            byte[] Functional_ID_bytes = new byte[2];
            data_buf.readBytes(Functional_ID_bytes);
            String Functional_ID = ToHexTool.bytes2HexString(Functional_ID_bytes);
            holloDomain.setFunctional_ID(Functional_ID);

            // 消息属性strat
            byte[] message_property_bytes_success = new byte[2];
            data_buf.readBytes(message_property_bytes_success);
            // 分包标识
            int subpackage_flog_success = message_property_bytes_success[0] & 0x20;
            holloDomain.setSubpackage_flog(subpackage_flog_success);
            // 消息体加密方式
            int message_body_encrypt = message_property_bytes_success[0] & 0x1C;
            holloDomain.setMessage_body_encrypt(Integer.toBinaryString(message_body_encrypt));
            // 消息体长度
            int message_type_1 = message_property_bytes_success[0] & 0x03;
            int message_type_2 = message_property_bytes_success[1] & 0xFF;
            int message_body_length = message_type_1 + message_type_2;
            holloDomain.setMessage_body_length(message_body_length);
            // 消息属性end

            // 设备ID
            int device_ID_1 = data_buf.readByte();
            char device_ID_ascii = (char) device_ID_1;
            String device_ID_2 = ToHexTool.HexString(data_buf.readUnsignedInt());
            String device_ID_3 = ToHexTool.HexString(data_buf.readUnsignedShort());
            String device_ID = "" + device_ID_ascii + device_ID_2 + device_ID_3;
            holloDomain.setDevice_ID(device_ID);

            // 消息流水号
            byte[] message_number_bytes = new byte[2];
            data_buf.readBytes(message_number_bytes);
            String message_number_str = ToHexTool.bytes2HexString(message_number_bytes);
            holloDomain.setMessage_number(message_number_str);

            // 是否有消息包封装顶
            if (subpackage_flog_success == 1) {
                // 消息包总数
                int message_page_total = data_buf.readUnsignedShort();
                holloDomain.setMessage_page_total(message_page_total);
                // 消息包序号
                int message_page_number = data_buf.readUnsignedShort();
                holloDomain.setMessage_page_number(message_page_number);
            }

            // 数据包
            byte[] data_page_bytes = new byte[data_buf.readableBytes()];
            data_buf.readBytes(data_page_bytes);
            holloDomain.setData_page(data_page_bytes);

            // 释放资源
            data_buf.release();

            // 终端信息转换为java对象完成
            list.add(holloDomain);
        }
    }
}
