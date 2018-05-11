package com.liser.socket.codec;

import com.liser.socket.constant.HollooConstant;
import com.liser.socket.util.Protocol;
import com.liser.socket.util.ToHexTool;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 车葫芦-解码 1.0.0
 */
public class HollooDecoder extends ByteToMessageDecoder {

//    private Logger logger = LoggerFactory.getLogger(HollooDecoder.class);
    private Logger logger = Logger.getLogger(HollooDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        if (in instanceof EmptyByteBuf || in.readableBytes() < 0) {
            return;
        }

        // 消息体长度
        int message_body_length = 0;
        // 是否获取到完整数据标识
        boolean isSuccess = false;

        while (in.isReadable()) {
            //int readIndex = in.readerIndex();
            in.markReaderIndex(); // 标记开始位置
            byte[] identifier_start_byte = new byte[1];
            in.readBytes(identifier_start_byte);
            String identifier_start_str = ToHexTool.bytes2HexString(identifier_start_byte);
            if (!identifier_start_str.equals("E7")) {
                in.clear();
                // in.release();
                // in.retain();
                //("客户端："+ ctx.channel().id() +"消息开始标识不正确！");
                return;
            }

            byte[] message_head_bytes = new byte[11];
            if (in.readableBytes() < 11) {
                in.clear();
                //("客户端："+ ctx.channel().id() +"消息头长度不够！");
                return;
            }
            in.readBytes(message_head_bytes);

            // 转义
            String message_head_str = ToHexTool.bytes2HexString(message_head_bytes);
            message_head_str = Protocol._inputDataEscape(message_head_str);
            byte[] message_head_buf = ToHexTool.hexString2Bytes(message_head_str);

            // 消息属性
            byte[] message_property_bytes = {message_head_buf[2], message_head_buf[3]};
            // 分包标识
            int subpackage_flog = message_property_bytes[0] & 0x20;
            // 消息体长度
            int message_type_1 = ((message_property_bytes[0] & 0x03)<<8)|0x00FF;
//            int message_type_2 = message_property_bytes[1] & 0xFF;
            message_body_length = message_type_1&(message_property_bytes[1]|0xFF00);
            logger.info("消息体长度："+message_body_length);
            // 分包标识
            if (subpackage_flog == 1) {
                message_body_length -= 4;
                if (in.readableBytes() < 6) {
                    in.clear();
                    message_body_length = 0;
                    //("客户端："+ ctx.channel().id() +"分包标志为【1】但无法获取消息包封装项!");
                    return;
                }
                in.readBytes(6);
            } else {
                if (in.readableBytes() < 2) {
                    in.clear();
                    message_body_length = 0;
                    //("客户端："+ ctx.channel().id() +"无法获取消息流水号!");
                    return;
                }
                in.readBytes(2);
            }

            // 验证消息体数据是否还够读取
            if (in.readableBytes() < message_body_length) {
                in.clear();
                message_body_length = 0;
                //("客户端："+ ctx.channel().id() +"可读消息体长度小于消息头中规定长度!");
                return;
            }
            byte[] message_body_bytes = new byte[message_body_length];
            in.readBytes(message_body_bytes);

            // 验证是否有检验位和消息标识尾
            if (in.readableBytes() < 2) {
                in.clear();
                message_body_length = 0;
                //("客户端："+ ctx.channel().id() +"无法获取校验位和消息结束标识!");
                return;
            }

            //获取到消息体长度，重置in的读取下标退出循环
            isSuccess = true;
            break;
        }

        // 获取到一个完整的数据，则将数据传递下去
        if (isSuccess) {
            // 重置in的下标，根据消息长度获取一条完整的消息
            in.resetReaderIndex();

            // 去掉消息标识头
            byte identifier_start = in.readByte();
            byte[] identifier_start_byte = {identifier_start};
            String identifier_startStr = ToHexTool.bytes2HexString(identifier_start_byte);

            // 读取我们需要的消息数据
            int useful_length = (HollooConstant.FIXED_LENGTH  - 3) + message_body_length;
            byte[] message_useful_bytes = new byte[useful_length];
            in.readBytes(message_useful_bytes);

            // 转义
            String message_useful_hexstr = ToHexTool.bytes2HexString(message_useful_bytes);
            message_useful_hexstr = Protocol._inputDataEscape(message_useful_hexstr);
            byte[] message_escape_bytes = ToHexTool.hexString2Bytes(message_useful_hexstr);

            // 读取校验位进行校验
            byte check_byte_read = in.readByte();
            logger.info("原始校验位："+check_byte_read);

            // 丢掉消息标识尾
            byte identifier_end = in.readByte();

            // 异或后的校验位
            byte check_byte_xor = Protocol.checkCode(message_escape_bytes); // 不包含检验位数组
            logger.info("异或出来的校验位："+check_byte_xor);

            // 原始数据组装打印
            byte[] identifier_end_byte = {check_byte_read,identifier_end};
            String identifier_endStr = ToHexTool.bytes2HexString(identifier_end_byte);
            String primitive = identifier_startStr + message_useful_hexstr + identifier_endStr;
            logger.info("接收到客户端数据："+primitive);

            // 进行校验
            if (check_byte_xor != check_byte_read) {
                // 校验失败

                // 不包含消息体和一头一尾还有校验位的(消息头+自定义一个byte的消息体数据)
                byte[] message_head = new byte[14];
                for (int i=0; i<13; i++){
                    message_head[i] = message_escape_bytes[i];
                }
                // 将客户端原始数据替换为应答结果(设置失败)
                message_head[13] = ToHexTool.hexString2Bytes("FF")[0];
                // 调用下一个业务系统处理
                ctx.fireChannelRead(message_head);
                return;

            }
            // 校验成功
            // 添加到下一个handle处理
            out.add(message_escape_bytes);

        }
    }
}
