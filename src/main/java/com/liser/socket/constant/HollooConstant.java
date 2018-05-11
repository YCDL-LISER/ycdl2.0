package com.liser.socket.constant;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HollooConstant {

    public static int FIXED_LENGTH = 16; // 消息固定长度

    // 主动上报
    public static String general_respone_head = "8001"; // 平台通用应答_下行
    public static String endpointlogin_respone_head = "8102"; // 终端登陆应答_下行
    public static String heart_up_head = "0002"; // 终端心跳上报_上行
    public static String error_down_head = "00EE"; // 终端应答下行格式错误_上行
    public static String login_up_head = "0102"; // 终端登录_上行
    public static String data_up_head = "0250"; // 终端上报车辆数据(全平台版)
    public static String alarm_up_head = "0251"; // 终端上报报警数据(全平台版)
    public static String behavior_up_head = "0204"; // 终端上报行程数据_上行
    public static String alarm_info_up_head = "0205"; // 终端上报报警描述信息_上行
    public static String base_station2G_up_head = "0207"; // 终端上报基站定位数据_上行
    public static String vehicle_trouble_up_head = "0209"; // 终端上报车辆故障_上行
    public static String base_station4G_up_head = "0218"; // 终端上报基站定位数据(4G专用)_上行
    public static String base_station_data_up_head = "0219"; // 终端上报单基站定位数据包(标准单基站包)_上行
    public static String sleep_awaken_data_up_head = "0212"; // 终端上报睡眠唤醒_上行
    public static String sleep_info_up_head = "0213"; // 终端上报睡眠进入_上行
    public static String gps_up_head = "0202"; // 终端上报GPS密集数据（定制）_上行
    public static String can_up_head = "0208"; // 终端上报CAN密集数据（定制）_上行
    public static String MAC_up_head = "0217"; // 终端上报WIFI 连接MAC数据（专用）_上行
    public static String temperature_up_head = "0215"; // 终端上报温度传感器数据（定制）_上行
    public static String rotate_inductor_up_head = "0216"; // 终端上报正反转传感器数据（定制）_上行
    // 终端加速度值大于预设值时上报XYZ轴对应数据(默认1200mg)
    public static String XYZ_up_head = "022A"; // 碰撞时XYZ数据上传（UK定制）_上行
    public static String driver_info_up_head = "020A"; // 终端上报当前驾驶员信息_上行
    public static String oil_up_head = "020B"; // 终端上报油量数据_上行
    public static String pic_up_head = "0230"; // 终端上报图片数据（定制）_上行
    public static String blindspot_up_head = "0704"; // 盲点数据补传_上行
    public static String blindspot_page_up = "0750"; // 盲点车辆数据包上报(全平台版)

    // 被动接收
    public static String endpoint_common_response = "0001"; // 终端通用应答_上行
    public static String remote_call = "0255"; // 远程点名_上行
    public static String remote_query = "0300"; // 远程查询_上行
    public static String remote_update = "0502"; // 远程升级_上行
    public static String remote_control_answer = "0600"; // 终端远程控制应答_上行
    public static String remote_textCommand_answer = "0700"; // 远程文本指令回复_上行
    public static String up_varnish_transmission = "8900"; // 上行透传_上行


}
