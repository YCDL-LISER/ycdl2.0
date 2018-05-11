package com.liser.socket.dao;

import com.liser.socket.bean.DeviceLogWithBLOBs;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeviceLogMapper {
    int insert(DeviceLogWithBLOBs record);

    int insertSelective(DeviceLogWithBLOBs record);
}