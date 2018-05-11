package com.liser.socket.dispose;

import com.liser.common.service.ServiceLocator;
import com.liser.common.util.DateUtil;
import com.liser.common.util.ValidateUtil;
import com.liser.socket.bean.DeviceLogWithBLOBs;
import com.liser.socket.service.DeviceService;
import com.liser.socket.util.ToHexTool;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.HashMap;
import java.util.Map;


public class EPReportDataDispose extends HolloDisposeAdapter<DeviceLogWithBLOBs> {

    private DeviceService deviceService = (DeviceService) ServiceLocator.getService("deviceService");
    private ByteBuf byteBuf;

    public EPReportDataDispose() {

    }

    /**
     * 【0250】/【0251】终端上报车辆/报警数据(全平台版)
     *
     * @param data
     * @param device
     * @throws Exception
     */
    public void dispose(byte[] data, DeviceLogWithBLOBs device) throws Exception {
        byteBuf = Unpooled.buffer(data.length);
        byteBuf.writeBytes(data);

        // 数据内容
        device.setContent(ToHexTool.bytes2HexString(data));

        // SysTime
        byte year = (byte) byteBuf.readUnsignedByte(); // 年
        byte month = (byte) byteBuf.readUnsignedByte(); // 月
        byte day = (byte) byteBuf.readUnsignedByte(); // 日
        byte hour = (byte) byteBuf.readUnsignedByte(); // 时
        byte minute = (byte) byteBuf.readUnsignedByte(); // 分
        byte seconds = (byte) byteBuf.readUnsignedByte(); // 秒
        String systime = DateUtil.toTimeString(year, month, day, hour, minute, seconds);
        device.setGatherTime(DateUtil.stringToSqlTimestamp(systime));

        // 驾驶循环标签
        byte trip_mark = (byte) byteBuf.readUnsignedShort();

        // ACC Status
        byte acc_status = (byte) byteBuf.readUnsignedByte();

        // 报警类别ID(0x0000表示非报警数据包/0xXXXX表示报警数据包)
        int alm_ID = byteBuf.readUnsignedShort();
        String alm_IDStr = Integer.toHexString(alm_ID);
        device.setWarningId(alm_IDStr);

        /***** GPS信息(20字节)start *****/
        // Valid Byte(Bit0 Gps定位是否有效 1/0 是/否)
        int valid_byte = byteBuf.readUnsignedByte();
        valid_byte = valid_byte & 0x01;
        if (valid_byte == 1) {
            device.setHasGps(true);
        } else {
            device.setHasGps(false);
        }
        //  纬度start
        long Latitude = byteBuf.readUnsignedInt();
        // (0北纬/1南纬)
        long Latitude_status = Latitude & 0x80000000;
        if (Latitude_status == 1) {
            device.setLatitudeNs(true);
        } else {
            device.setLatitudeNs(false);
        }
        Latitude = Latitude & 0x7FFFFFFF;
        double Latitudes = (double) Latitude / 1000000; // 纬度end
        device.setLatitude(Double.toString(Latitudes));

        // 经度start
        long Longitude = byteBuf.readUnsignedInt();
        // 0/东经 1/西经
        long Longitude_status = Longitude & 0x80000000;
        if (Longitude_status == 1) {
            device.setLongitudeEw(true);
        } else {
            device.setLongitudeEw(false);
        }
        Longitude = Longitude & 0x7FFFFFFF;
        double Longitudes = (double) Longitude * 0.000001; // 经度end
        device.setLongitude(Double.toString(Longitudes));

        // 高度(单位/米)
        long Altitude = byteBuf.readUnsignedInt();
        float Altitudes = Altitude / 10; // 高度end

        // 卫星数
        byte Satellites = (byte) byteBuf.readUnsignedByte();

        // 速度(km/h)
        float speed = (float) byteBuf.readUnsignedShort();
        device.setSpeed(speed);

        // 方向(度)
        int direction = byteBuf.readUnsignedShort();
        float directions = (float) direction / 10; // 角度end
        device.setDirection(directions);

        // 位置精度
        int pdop = byteBuf.readUnsignedShort();
        double pdops = (double) pdop / 100; // 位置精度end
        /***** GPS信息(20字节)end *****/

        // 车型ID表
        long car_ID = byteBuf.readUnsignedInt();
        // OBD协议类别
        byte OBD_type = (byte) byteBuf.readUnsignedByte();

        // 动态信息组包
        byte[] dynamicID = new byte[2];
        String dynamicIDHex = "";
        Map<String, byte[]> dynamicDataMap = new HashMap<String, byte[]>(88);
        while (byteBuf.isReadable()) {
            byteBuf.readBytes(dynamicID);
            dynamicIDHex = ToHexTool.bytes2HexString(dynamicID);
            int length = byteBuf.readUnsignedShort();
            byte[] dynamicContent = new byte[length];
            byteBuf.readBytes(dynamicContent);
            dynamicDataMap.put(dynamicIDHex, dynamicContent);
        }

        // 释放资源
        byteBuf.release();

        // 录入日志数据
        if (valid_byte == 1) {
            // gps定位有效，保存登陆日志
            device.setCreateTime(DateUtil.getCurrentDateTime());
            deviceService.saveDeviceLogMsg(device);
        }

        // 更新车辆信息
        byte[] car_status_bytes = dynamicDataMap.get("0009"); // 车辆状态
        byte[] continuation_line_bytes = dynamicDataMap.get("7001"); // 续航里程
        byte[] dump_energy_bytes = dynamicDataMap.get("7002"); // 剩余电量
        Map<String, Object> updateMap = new HashMap<String, Object>(5);
        if (!ValidateUtil.isEmpty(car_status_bytes)) {
            // 车辆状态
            String carStatusCode = ToHexTool.bytes2HexString(car_status_bytes);
            updateMap.put("carStatusCode", carStatusCode);
        }
        if (!ValidateUtil.isEmpty(continuation_line_bytes)) {
            // 续航里程
            int continuationLineInt = ToHexTool.bytes2int(continuation_line_bytes);
            float continuationLine = (float) continuationLineInt / 10;
            updateMap.put("continuationLine", continuationLine);
        }
        if (!ValidateUtil.isEmpty(dump_energy_bytes)) {
            // 剩余电量
            int dumpEnergy = ToHexTool.bytes2int(dump_energy_bytes);
            updateMap.put("dumpEnergy", dumpEnergy);
        }
        if (!ValidateUtil.isEmpty(updateMap)) {
            deviceService.updateCarStatus(updateMap, device.getDeviceNumber());
        }

        //

    }

}

