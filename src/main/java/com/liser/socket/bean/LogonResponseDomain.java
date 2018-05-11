package com.liser.socket.bean;

import java.sql.Date;

/**
 * 终端版本信息包
 */
public class LogonResponseDomain {
    // 终端版本号
    private String endpoint_version;
    // 终端软件版本日期
    private Date endpoint_versionDate;
    private String CPU_ID;
    private String GSM_type_name;
    private String GSM_IMEI;
    private String SIM_IMSI;
    private String SIM_ICCID;
    // 车系车型ID
    private int car_type;
    // 汽车VIN码
    private String VIN;
    // 总里程
    private long total_distance;
    // 总耗油量
    private long total_oil_consumption;

    // 平台当前时间
    private String  plateform_date;
    // 排量
    private int displacement;
    // 是否升级
    private String upgrade;

    public String getEndpoint_version() {
        return endpoint_version;
    }

    public void setEndpoint_version(String endpoint_version) {
        this.endpoint_version = endpoint_version;
    }

    public Date getEndpoint_versionDate() {
        return endpoint_versionDate;
    }

    public void setEndpoint_versionDate(Date endpoint_versionDate) {
        this.endpoint_versionDate = endpoint_versionDate;
    }

    public String getCPU_ID() {
        return CPU_ID;
    }

    public void setCPU_ID(String CPU_ID) {
        this.CPU_ID = CPU_ID;
    }

    public String getGSM_type_name() {
        return GSM_type_name;
    }

    public void setGSM_type_name(String GSM_type_name) {
        this.GSM_type_name = GSM_type_name;
    }

    public String getGSM_IMEI() {
        return GSM_IMEI;
    }

    public void setGSM_IMEI(String GSM_IMEI) {
        this.GSM_IMEI = GSM_IMEI;
    }

    public String getSIM_IMSI() {
        return SIM_IMSI;
    }

    public void setSIM_IMSI(String SIM_IMSI) {
        this.SIM_IMSI = SIM_IMSI;
    }

    public String getSIM_ICCID() {
        return SIM_ICCID;
    }

    public void setSIM_ICCID(String SIM_ICCID) {
        this.SIM_ICCID = SIM_ICCID;
    }

    public int getCar_type() {
        return car_type;
    }

    public void setCar_type(int car_type) {
        this.car_type = car_type;
    }

    public String getVIN() {
        return VIN;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

    public long getTotal_distance() {
        return total_distance;
    }

    public void setTotal_distance(long total_distance) {
        this.total_distance = total_distance;
    }

    public long getTotal_oil_consumption() {
        return total_oil_consumption;
    }

    public void setTotal_oil_consumption(long total_oil_consumption) {
        this.total_oil_consumption = total_oil_consumption;
    }

    public String getPlateform_date() {
        return plateform_date;
    }

    public void setPlateform_date(String plateform_date) {
        this.plateform_date = plateform_date;
    }

    public int getDisplacement() {
        return displacement;
    }

    public void setDisplacement(int displacement) {
        this.displacement = displacement;
    }

    public String getUpgrade() {
        return upgrade;
    }

    public void setUpgrade(String upgrade) {
        this.upgrade = upgrade;
    }
}
