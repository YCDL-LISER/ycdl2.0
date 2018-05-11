package com.liser.socket.bean;

import java.util.Date;

public class DeviceLog {
    private String deviceNumber;

    private String funId;

    private String messageId;

    private String warningId;

    private Boolean hasGps;

    private String longitude;

    private Boolean longitudeEw;

    private String latitude;

    private Boolean latitudeNs;

    private Float speed;

    private Float direction;

    private Date gatherTime;

    private Date createTime;

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

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId == null ? null : messageId.trim();
    }

    public String getWarningId() {
        return warningId;
    }

    public void setWarningId(String warningId) {
        this.warningId = warningId == null ? null : warningId.trim();
    }

    public Boolean getHasGps() {
        return hasGps;
    }

    public void setHasGps(Boolean hasGps) {
        this.hasGps = hasGps;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude == null ? null : longitude.trim();
    }

    public Boolean getLongitudeEw() {
        return longitudeEw;
    }

    public void setLongitudeEw(Boolean longitudeEw) {
        this.longitudeEw = longitudeEw;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? null : latitude.trim();
    }

    public Boolean getLatitudeNs() {
        return latitudeNs;
    }

    public void setLatitudeNs(Boolean latitudeNs) {
        this.latitudeNs = latitudeNs;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public Float getDirection() {
        return direction;
    }

    public void setDirection(Float direction) {
        this.direction = direction;
    }

    public Date getGatherTime() {
        return gatherTime;
    }

    public void setGatherTime(Date gatherTime) {
        this.gatherTime = gatherTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}