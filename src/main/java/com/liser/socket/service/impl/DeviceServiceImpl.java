package com.liser.socket.service.impl;

import com.liser.common.dao.BaseDao;
import com.liser.common.util.DateUtil;
import com.liser.common.util.ValidateUtil;
import com.liser.socket.bean.DeviceCmd;
import com.liser.socket.bean.DeviceLogWithBLOBs;
import com.liser.socket.bean.DeviceWithBLOBs;
import com.liser.socket.dao.DeviceCmdMapper;
import com.liser.socket.dao.DeviceLogMapper;
import com.liser.socket.dao.DeviceMapper;
import com.liser.socket.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service("deviceService")
public class DeviceServiceImpl implements DeviceService {

    @Resource(name = "baseDao")
    BaseDao baseDao;

    @Autowired
    DeviceMapper deviceMapper;

    @Autowired
    DeviceLogMapper deviceLogMapper;

    @Autowired
    DeviceCmdMapper deviceCmdMapper;

    /**
     * 查询设备ID是否已存在
     * @param device
     * @return
     */
    public int queryDeviceExist(DeviceWithBLOBs device) {
        int exist = 0;
        DeviceWithBLOBs diviceLog = deviceMapper.selectByDeviceId(device);
        if (ValidateUtil.isEmpty(diviceLog)){
            return exist;
        }
        return diviceLog.getId();
    }

    /**
     * 车辆数据保存
     * @param device
     * @return
     */
    @Transactional
    public boolean saveDeviceMsg(DeviceWithBLOBs device) {
        int count = deviceMapper.insert(device);
        if (count == 1){
            return true;
        }
        return false;
    }

    /**
     * 更新车辆状态信息
     * @param device
     * @return
     */
    @Transactional
    public boolean updateDeviceMsg(DeviceWithBLOBs device) {
        int count = deviceMapper.updateByPrimaryKeySelective(device);
        if (count == 1){
            return true;
        }
        return false;
    }

    /**
     * 车辆数据日志写入
     * @param deviceLog
     * @return
     */
    @Transactional
    public boolean saveDeviceLogMsg(DeviceLogWithBLOBs deviceLog) {
        /*try {
            baseDao.insert("DeviceLogMapper.insertSelective",deviceLog);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        int count = deviceLogMapper.insertSelective(deviceLog);
        if (count == 1){
            return true;
        }
        return false;
    }

    /**
     * 更新车辆状态
     * @param updateMap
     * @param deviceNumber
     * @return
     * @throws Exception
     */
    @Transactional
    public boolean updateCarStatus(Map updateMap, String deviceNumber) throws Exception {
        updateMap.put("updateTime", DateUtil.getCurrentDateTime());
        updateMap.put("deviceNumber", deviceNumber);

        int count = (Integer) baseDao.update("com.liser.socket.dao.DeviceMapper.updateCarStatus",updateMap);
        if (count == 1){
            return true;
        }
        return false;
    }

    /**
     * 更新设备在线/离线状态
     * @param deviceID
     * @param device_online
     * @return
     * @throws Exception
     */
    public boolean updateDeviceOnline(String deviceID, int device_online) throws Exception {
        Map<String, Object> updateMap = new HashMap<String, Object>();
        updateMap.put("deviceOnline", device_online);
        updateMap.put("updateTime", DateUtil.getCurrentDateTime());
        updateMap.put("deviceNumber", deviceID);

        int count = (Integer) baseDao.update("com.liser.socket.dao.DeviceMapper.updateDeviceOnline",updateMap);
        if (count == 1){
            return true;
        }
        return false;
    }

    /**
     * 更新设备应答结果
     * @param deviceCmd
     * @return
     * @throws Exception
     */
    public boolean updateDeviceResponse(DeviceCmd deviceCmd) throws Exception {
        DeviceCmd result = deviceCmdMapper.selectCmdExist(deviceCmd);
        if (ValidateUtil.isEmpty(result)){
            // 新增
            deviceCmdMapper.insert(deviceCmd);
        }else {
            // 更新
            deviceCmd.setId(result.getId());
            deviceCmdMapper.updateByPrimaryKey(deviceCmd);
        }
        return true;
    }
}
