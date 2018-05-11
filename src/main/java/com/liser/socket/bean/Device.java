package com.liser.socket.bean;

import java.util.Date;

public class Device {
    private Integer id;

    private String name;

    private Integer goodsId;

    private String deviceNumber;

    private Byte deviceType;

    private Byte deviceStatus;

    private Byte deviceOnline;

    private String cpuId;

    private String gsmType;

    private String gsmImei;

    private String simImsi;

    private String simIccid;

    private String obdVersions;

    private Date obdVersionsTime;

    private String carType;

    private String carStatusCode;

    private Float drivingMileage;

    private Byte energy;

    private String vin;

    private String odometer;

    private String oil;

    private Date createTime;

    private Date updateTime;

    private Integer storeKeyId;

    private String storeKeyName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber == null ? null : deviceNumber.trim();
    }

    public Byte getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Byte deviceType) {
        this.deviceType = deviceType;
    }

    public Byte getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(Byte deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public Byte getDeviceOnline() {
        return deviceOnline;
    }

    public void setDeviceOnline(Byte deviceOnline) {
        this.deviceOnline = deviceOnline;
    }

    public String getCpuId() {
        return cpuId;
    }

    public void setCpuId(String cpuId) {
        this.cpuId = cpuId == null ? null : cpuId.trim();
    }

    public String getGsmType() {
        return gsmType;
    }

    public void setGsmType(String gsmType) {
        this.gsmType = gsmType == null ? null : gsmType.trim();
    }

    public String getGsmImei() {
        return gsmImei;
    }

    public void setGsmImei(String gsmImei) {
        this.gsmImei = gsmImei == null ? null : gsmImei.trim();
    }

    public String getSimImsi() {
        return simImsi;
    }

    public void setSimImsi(String simImsi) {
        this.simImsi = simImsi == null ? null : simImsi.trim();
    }

    public String getSimIccid() {
        return simIccid;
    }

    public void setSimIccid(String simIccid) {
        this.simIccid = simIccid == null ? null : simIccid.trim();
    }

    public String getObdVersions() {
        return obdVersions;
    }

    public void setObdVersions(String obdVersions) {
        this.obdVersions = obdVersions == null ? null : obdVersions.trim();
    }

    public Date getObdVersionsTime() {
        return obdVersionsTime;
    }

    public void setObdVersionsTime(Date obdVersionsTime) {
        this.obdVersionsTime = obdVersionsTime;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType == null ? null : carType.trim();
    }

    public String getCarStatusCode() {
        return carStatusCode;
    }

    public void setCarStatusCode(String carStatusCode) {
        this.carStatusCode = carStatusCode == null ? null : carStatusCode.trim();
    }

    public Float getDrivingMileage() {
        return drivingMileage;
    }

    public void setDrivingMileage(Float drivingMileage) {
        this.drivingMileage = drivingMileage;
    }

    public Byte getEnergy() {
        return energy;
    }

    public void setEnergy(Byte energy) {
        this.energy = energy;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin == null ? null : vin.trim();
    }

    public String getOdometer() {
        return odometer;
    }

    public void setOdometer(String odometer) {
        this.odometer = odometer == null ? null : odometer.trim();
    }

    public String getOil() {
        return oil;
    }

    public void setOil(String oil) {
        this.oil = oil == null ? null : oil.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStoreKeyId() {
        return storeKeyId;
    }

    public void setStoreKeyId(Integer storeKeyId) {
        this.storeKeyId = storeKeyId;
    }

    public String getStoreKeyName() {
        return storeKeyName;
    }

    public void setStoreKeyName(String storeKeyName) {
        this.storeKeyName = storeKeyName == null ? null : storeKeyName.trim();
    }
}