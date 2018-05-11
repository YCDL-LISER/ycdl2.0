package com.liser.socket.dao;

import com.liser.socket.bean.DeviceCmd;

public interface DeviceCmdMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DeviceCmd record);

    int insertSelective(DeviceCmd record);

    DeviceCmd selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DeviceCmd record);

    int updateByPrimaryKey(DeviceCmd record);

    DeviceCmd selectCmdExist(DeviceCmd record);

}