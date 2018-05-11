package com.liser.socket.bean;

public class ControlAnswerDomain {
    // 设备ID(更新车辆状态需要)
    public String deviceID;

    // 控制ID
    public String controlID;
    // 控制循环大序列号
    public short bigSerialNumber;
    // 控制循环小序列号
    public byte smallSerialNumber;
    // 控制结果
    public String controlResults;
    // 车辆状态
    private byte[] carStatusCode;

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getControlID() {
        return controlID;
    }

    public void setControlID(String controlID) {
        this.controlID = controlID;
    }

    public short getBigSerialNumber() {
        return bigSerialNumber;
    }

    public void setBigSerialNumber(short bigSerialNumber) {
        this.bigSerialNumber = bigSerialNumber;
    }

    public byte getSmallSerialNumber() {
        return smallSerialNumber;
    }

    public void setSmallSerialNumber(byte smallSerialNumber) {
        this.smallSerialNumber = smallSerialNumber;
    }

    public String getControlResults() {
        return controlResults;
    }

    public void setControlResults(String controlResults) {
        this.controlResults = controlResults;
    }

    public  byte[] getCarStatusCode() {
        return carStatusCode;
    }

    public void setCarStatusCode( byte[] carStatusCode) {
        this.carStatusCode = carStatusCode;
    }
}
