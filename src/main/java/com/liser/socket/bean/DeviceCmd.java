package com.liser.socket.bean;

import java.util.Date;

public class DeviceCmd {
    private Integer id;

    private String deviceNumber;

    private String funId;

    private String cmdId;

    private String cmdStatus;

    private Date time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber == null ? null : deviceNumber.trim();
    }

    public String getFunId() {
        return funId;
    }

    public void setFunId(String funId) {
        this.funId = funId == null ? null : funId.trim();
    }

    public String getCmdId() {
        return cmdId;
    }

    public void setCmdId(String cmdId) {
        this.cmdId = cmdId == null ? null : cmdId.trim();
    }

    public String getCmdStatus() {
        return cmdStatus;
    }

    public void setCmdStatus(String cmdStatus) {
        this.cmdStatus = cmdStatus == null ? null : cmdStatus.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}