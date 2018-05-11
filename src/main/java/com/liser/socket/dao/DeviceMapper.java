package com.liser.socket.dao;

import com.liser.socket.bean.Device;
import com.liser.socket.bean.DeviceWithBLOBs;

public interface DeviceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DeviceWithBLOBs record);

    int insertSelective(DeviceWithBLOBs record);

    DeviceWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DeviceWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(DeviceWithBLOBs record);

    int updateByPrimaryKey(Device record);

    DeviceWithBLOBs selectByDeviceId(DeviceWithBLOBs record);
}