package com.liser.socket.dispose;

import com.liser.common.service.ServiceLocator;
import com.liser.common.util.DateUtil;
import com.liser.common.util.ValidateUtil;
import com.liser.socket.bean.ControlAnswerDomain;
import com.liser.socket.bean.DeviceCmd;
import com.liser.socket.constant.HollooConstant;
import com.liser.socket.service.DeviceService;
import com.liser.socket.util.ToHexTool;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.HashMap;
import java.util.Map;

/**
 * 【0600】终端远程控制应答处理
 */
public class EPControlAnswerDispose extends HolloDisposeAdapter<ControlAnswerDomain> {
    private DeviceService deviceService = (DeviceService) ServiceLocator.getService("deviceService");
    private ByteBuf byteBuf;

    public void dispose(byte[] data, ControlAnswerDomain controlAnswerDomain) throws Exception {
        byteBuf = Unpooled.buffer(data.length);
        byteBuf.writeBytes(data);

        DeviceCmd deviceCmd = new DeviceCmd();
        deviceCmd.setFunId(HollooConstant.remote_control_answer); // 功能ID
        deviceCmd.setDeviceNumber(controlAnswerDomain.getDeviceID());

        // 控制命令ID
        byte[] controlID_bytes = new byte[2];
        byteBuf.readBytes(controlID_bytes);
        String controlID = ToHexTool.bytes2HexString(controlID_bytes);
        deviceCmd.setCmdId(controlID);

        // 控制循环大序列号
        short bigSerialNumber = (short) byteBuf.readUnsignedShort();

        // 控制循环小序列号
        byte smallSerialNumber = (byte) byteBuf.readUnsignedByte();

        // 控制结果
        byte[] controlResults_bytes = new byte[1];
        byteBuf.readBytes(controlResults_bytes);
        String controlResults = ToHexTool.bytes2HexString(controlResults_bytes);
        deviceCmd.setCmdStatus(controlResults);

        // 车辆状态
        byte[] carStatusCode_bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(carStatusCode_bytes);

        // 释放资源
        byteBuf.release();

        // 命令回复时间
        deviceCmd.setTime(DateUtil.getCurrentDateTime());

        // 更新设备命令回复结果
        deviceService.updateDeviceResponse(deviceCmd);

        // 更新车辆状态信息
        if (!ValidateUtil.isEmpty(carStatusCode_bytes)){
            String carStatusCode = ToHexTool.bytes2HexString(carStatusCode_bytes);
            Map<String,String> updataMap = new HashMap<String, String>();
            updataMap.put("carStatusCode",carStatusCode);
            deviceService.updateCarStatus(updataMap, controlAnswerDomain.getDeviceID());
        }

    }
}
