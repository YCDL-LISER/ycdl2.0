package com.liser.socket.dispose;

import com.liser.common.service.ServiceLocator;
import com.liser.common.util.DateUtil;
import com.liser.socket.bean.DeviceWithBLOBs;
import com.liser.socket.bean.LogonResponseDomain;
import com.liser.socket.service.DeviceService;
import com.liser.socket.util.ToHexTool;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class EPLoginDispose extends HolloDisposeAdapter<DeviceWithBLOBs> {

    private LogonResponseDomain versionDomain;
    private ByteBuf byteBuf;
    private DeviceService deviceService = (DeviceService) ServiceLocator.getService("deviceService");

    public EPLoginDispose(){

    }

    public EPLoginDispose(LogonResponseDomain versionDomain){
        this.versionDomain = versionDomain;
    }


    /**
     * 【0102】终端登录-处理
     * @param data
     * @throws Exception
     */
    public void dispose(byte[] data, DeviceWithBLOBs device) throws Exception {
        byteBuf = Unpooled.buffer(data.length);
        byteBuf.writeBytes(data);

        // 登录消息包原始数据
        device.setContent(ToHexTool.bytes2HexString(data));

        // 终端版本号
        byte[] endpoint_version_bytes = new byte[14];
        byteBuf.readBytes(endpoint_version_bytes);
        String endpoint_version = new String(endpoint_version_bytes,"GBK");
        versionDomain.setEndpoint_version(endpoint_version);
        device.setObdVersions(endpoint_version);

        // 终端软件版本日期
        byte[] endpoint_versionDate_bytes = new byte[10];
        byteBuf.readBytes(endpoint_versionDate_bytes);
        String endpoint_versionDateStr = new String(endpoint_versionDate_bytes,"GBK");
        java.sql.Date endpoint_versionDate = DateUtil.stringToSqlDate(endpoint_versionDateStr);
        versionDomain.setEndpoint_versionDate(endpoint_versionDate);
        device.setObdVersionsTime(endpoint_versionDate);

        // cpu ID号
        byte[] cpu_ID_bytes = new byte[12];
        byteBuf.readBytes(cpu_ID_bytes);
        String cpu_ID = ToHexTool.bytes2HexString(cpu_ID_bytes);
        versionDomain.setCPU_ID(cpu_ID);
        device.setCpuId(cpu_ID);

        // GSM TYPE Name
        byte[] gsm_typeName_bytes = new byte[15];
        byteBuf.readBytes(gsm_typeName_bytes);
        String gsm_typeName = new String(gsm_typeName_bytes,"GBK");
        versionDomain.setGSM_type_name(gsm_typeName);
        device.setGsmType(gsm_typeName);

        // GSM IMEI号
        byte[] gsm_imei_bytes = new byte[15];
        byteBuf.readBytes(gsm_imei_bytes);
        String gsm_imei = new String(gsm_imei_bytes,"GBK");
        versionDomain.setGSM_IMEI(gsm_imei);
        device.setGsmImei(gsm_imei);

        // SIM卡 IMSI号
        byte[] sim_imsi_bytes = new byte[15];
        byteBuf.readBytes(sim_imsi_bytes);
        String sim_imsi = new String(sim_imsi_bytes,"GBK");
        versionDomain.setSIM_IMSI(sim_imsi);
        device.setSimImsi(sim_imsi);

        // SIM卡 ICCID
        byte[] sim_iccid_bytes = new byte[20];
        byteBuf.readBytes(sim_iccid_bytes);
        String sim_iccid = new String(sim_iccid_bytes,"GBK");
        versionDomain.setSIM_ICCID(sim_iccid);
        device.setSimIccid(sim_iccid);

        // 车系车型ID
        int car_type = byteBuf.readUnsignedShort();
        versionDomain.setCar_type(car_type);
        device.setCarType(Integer.toString(car_type));

        // 汽车VIN码
        byte[] vin_bytes = new byte[17];
        byteBuf.readBytes(vin_bytes);
        String vin = new String(vin_bytes,"GBK");
        versionDomain.setVIN(vin);
        device.setVin(vin);

        // 总里程
        long total_distance = byteBuf.readUnsignedInt();
        versionDomain.setTotal_distance(total_distance);
        device.setOdometer(Long.toString(total_distance));

        // 总耗油量
        long total_oil_consumption = byteBuf.readUnsignedInt();
        versionDomain.setTotal_oil_consumption(total_oil_consumption);
        device.setOil(Long.toString(total_oil_consumption));

        // 设备在线状态：0掉线/1在线
        device.setDeviceOnline((byte)1);

        // 对数据库进行操作
        int result = deviceService.queryDeviceExist(device);
        if (result == 0){
            // 保存
            device.setCreateTime(DateUtil.getCurrentDateTime()); // 录入时间
            deviceService.saveDeviceMsg(device);
        }else {
            // 已存在，更新
            device.setId(result);
            device.setUpdateTime(DateUtil.getCurrentDateTime()); // 更新时间
            deviceService.updateDeviceMsg(device);
        }

        /*for(String key : WebSocketConstant.pushCtxMap.keySet()){
            ChannelHandlerContext ctx = WebSocketConstant.pushCtxMap.get(key);
            ctx.channel().writeAndFlush(new TextWebSocketFrame("服务收到数据时间："+ new Date()));
        }*/

        // 平台应答终端登录
        String systemDate = DateUtil.getCurrentTime("yyMMddHHmmss");
        versionDomain.setPlateform_date(systemDate); // 平台当前时间
        versionDomain.setDisplacement(1500); // 排量
        versionDomain.setUpgrade("05"); // 是否升级(0x55 升级，其他不升级)

        byteBuf.clear();

    }

}
