package com.liser.socket.service;

import com.liser.socket.bean.DeviceCmd;
import com.liser.socket.bean.DeviceLogWithBLOBs;
import com.liser.socket.bean.DeviceWithBLOBs;

import java.util.Map;

public interface DeviceService {

    /**
     * 查询对应的设备ID是否已经存在
     * @param device
     * @return
     */
    public int queryDeviceExist(DeviceWithBLOBs device);

    /**
     * 终端登录保存设备信息
     * @param device
     * @return
     */
    public boolean saveDeviceMsg(DeviceWithBLOBs device);

    /**
     * 更新设备车辆状态
     * @param device
     * @return
     */
    public boolean updateDeviceMsg(DeviceWithBLOBs device);

    /**
     * 保存设备日志信息
     * @param deviceLog
     * @return
     */
    public boolean saveDeviceLogMsg(DeviceLogWithBLOBs deviceLog);

    /**
     * 更新车辆状态
     * @param
     * @return
     */
    public boolean updateCarStatus(Map map, String deviceNumber) throws Exception;

    /**
     * 更新设备在线/离线状态
     * @param
     * @return
     */
    public boolean updateDeviceOnline(String deviceID, int device_online) throws Exception;

    /**
     * 更新设备应答结果
     * @param
     * @return
     */
    public boolean updateDeviceResponse(DeviceCmd deviceCmd) throws Exception;
}
