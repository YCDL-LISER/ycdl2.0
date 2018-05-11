package com.liser.socket.dispose;

import com.liser.common.util.ValidateUtil;
import com.liser.socket.bean.LogonResponseDomain;
import com.liser.socket.bean.HolloDomain;
import com.liser.socket.constant.HollooConstant;
import com.liser.socket.util.ToHexTool;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

public abstract class HolloDisposeAdapter<T> {

    public HolloDisposeAdapter(){

    }

    /**
     * 平台通用应答
     * @param ctx
     * @param holloDomain
     * @throws Exception
     */
    public static void systemGeneralResponse(ChannelHandlerContext ctx, HolloDomain holloDomain) throws Exception{

        // 准备参数
        String functional_ID = HollooConstant.general_respone_head;// 功能ID(8001平台通用应答_下行)
        String device_ID = holloDomain.getDevice_ID(); // 设备ID
        String message_number = holloDomain.getMessage_number();// 消息流水号
        String success = holloDomain.getSuccess();// 应答结果

        // 判断应答结果
        if(ValidateUtil.isEmpty(success)){
            // 设置成功
            success = "00";
        }

        // 消息体(消息体固定格式为：消息流水号+功能ID+应答结果)
        String send_functional_ID = holloDomain.getFunctional_ID();
        byte[] general_response_bytes = ToHexTool.hexString2Bytes(message_number + send_functional_ID + success);

        // 创建对象准备应答
        HolloDomain holloDomain_heart_up_head = new HolloDomain();
        // 功能ID
        holloDomain_heart_up_head.setFunctional_ID(functional_ID);
        // 消息属性start：平台通用应答默认为5个字节
        holloDomain_heart_up_head.setSubpackage_flog(0); // 不分包
        holloDomain_heart_up_head.setMessage_body_length(5); // 消息体长度
        // 消息属性end
        // 设备ID
        holloDomain_heart_up_head.setDevice_ID(device_ID);
        // 消息流水号
        holloDomain_heart_up_head.setMessage_number(message_number);
        // 消息体
        holloDomain_heart_up_head.setData_page(general_response_bytes);

        // 发送给客户端
        ctx.writeAndFlush(holloDomain_heart_up_head);
    }

    /**
     * 登录应答包
     * @param ctx
     * @param holloDomain
     * @param versionDomain
     * @throws Exception
     */
    public void endpointLoginResponse(ChannelHandlerContext ctx, HolloDomain holloDomain, LogonResponseDomain versionDomain) throws Exception{

        // 准备参数
        String functional_ID = HollooConstant.endpointlogin_respone_head;// 功能ID(8102终端登陆应答_下行)
        String device_ID = holloDomain.getDevice_ID(); // 设备ID
        String message_number = holloDomain.getMessage_number();// 消息流水号

        // 消息体start(消息体固定格式为：平台当前时间+车型ID+排量+是否升级)

        // 平台当前时间
        String plateform_date = versionDomain.getPlateform_date();
        byte year = (byte) Integer.parseInt(plateform_date.substring(0,2)); // 年
        byte month = (byte) Integer.parseInt(plateform_date.substring(2,4)); // 月
        byte day = (byte) Integer.parseInt(plateform_date.substring(4,6)); // 日
        byte time = (byte) Integer.parseInt(plateform_date.substring(6,8)); // 时
        byte minute = (byte) Integer.parseInt(plateform_date.substring(8,10)); // 分
        byte secend = (byte) Integer.parseInt(plateform_date.substring(10,12)); // 秒
        byte[] plateform_date_byte = {year, month, day, time, minute, secend};
        // 车型ID
        int car_type = versionDomain.getCar_type();
        // 排量
        int displacement = versionDomain.getDisplacement();
        // 是否升级
        String upgrade = versionDomain.getUpgrade();
        byte[] upgrade_byte = ToHexTool.hexString2Bytes(upgrade);

        // 临时bytebuf
        ByteBuf byteBuf = Unpooled.buffer(11);
        byteBuf.writeBytes(plateform_date_byte);
        byteBuf.writeShort(car_type);
        byteBuf.writeShort(displacement);
        byteBuf.writeBytes(upgrade_byte);
        // 消息体数组
        byte[] general_response_bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(general_response_bytes);

        // 创建对象准备应答
        HolloDomain holloDomain_heart_up_head = new HolloDomain();
        // 功能ID
        holloDomain_heart_up_head.setFunctional_ID(functional_ID);

        // 消息属性start：终端登陆应答默认为11个字节
        holloDomain_heart_up_head.setSubpackage_flog(0); // 不分包
        holloDomain_heart_up_head.setMessage_body_length(11); // 消息体长度
        // 消息属性end

        // 设备ID
        holloDomain_heart_up_head.setDevice_ID(device_ID);
        // 消息流水号
        holloDomain_heart_up_head.setMessage_number(message_number);
        // 消息体
        holloDomain_heart_up_head.setData_page(general_response_bytes);

        // 发送给客户端
        ctx.writeAndFlush(holloDomain_heart_up_head);

    }

    public abstract void dispose(byte[] data, T device) throws Exception;

}
