package com.liser.socket.bean;

import java.util.Arrays;

/**
 * 消息bean
 */
public class HolloDomain {

    // 消息标识start
    private String identifier_start;

    // 功能id
    private String functional_ID;

    /* 消息属性start */
    private byte[] message_property;
    // 分包标志
    private int subpackage_flog;
    // 消息体加密方式
    private String message_body_encrypt;
    // 消息体长度
    private int message_body_length;
    /* 消息属性end */

    // 设备id
    private String device_ID;

    // 消息流水号
    private String message_number;

    // 消息包封装顶start
    // 消息包总数
    private int message_page_total;
    // 消息包序号
    private int message_page_number;
    // 消息包封装顶end

    // 数据包
    private byte[] data_page;
    // 数据hex
    private String data_page_hex;

    // 校验
    private byte check;

    // 消息标识end
    private String identifier_end;

    // 应答结果
    private String success;


    public HolloDomain(){

    }

    public HolloDomain(String message_number, String functional_ID, String success){
        this.message_number = message_number;
        this.functional_ID = functional_ID;
        this.success = success;
    }

    public String getIdentifier_start() {
        return identifier_start;
    }

    public void setIdentifier_start(String identifier_start) {
        this.identifier_start = identifier_start;
    }

    public String getFunctional_ID() {
        return functional_ID;
    }

    public void setFunctional_ID(String functional_ID) {
        this.functional_ID = functional_ID;
    }

    public byte[] getMessage_property() {
        return message_property;
    }

    public void setMessage_property(byte[] message_property) {
        this.message_property = message_property;
    }

    public int getSubpackage_flog() {
        return subpackage_flog;
    }

    public void setSubpackage_flog(int subpackage_flog) {
        this.subpackage_flog = subpackage_flog;
    }


    public String getMessage_body_encrypt() {
        return message_body_encrypt;
    }

    public void setMessage_body_encrypt(String message_body_encrypt) {
        this.message_body_encrypt = message_body_encrypt;
    }

    public int getMessage_body_length() {
        return message_body_length;
    }

    public void setMessage_body_length(int message_body_length) {
        this.message_body_length = message_body_length;
    }

    public String getDevice_ID() {
        return device_ID;
    }

    public void setDevice_ID(String device_ID) {
        this.device_ID = device_ID;
    }

    public String getMessage_number() {
        return message_number;
    }

    public void setMessage_number(String message_number) {
        this.message_number = message_number;
    }

    public int getMessage_page_total() {
        return message_page_total;
    }

    public void setMessage_page_total(int message_page_total) {
        this.message_page_total = message_page_total;
    }

    public int getMessage_page_number() {
        return message_page_number;
    }

    public void setMessage_page_number(int message_page_number) {
        this.message_page_number = message_page_number;
    }

    public byte[] getData_page() {
        return data_page;
    }

    public void setData_page(byte[] data_page) {
        this.data_page = data_page;
    }

    public String getData_page_hex() {
        return data_page_hex;
    }

    public void setData_page_hex(String data_page_hex) {
        this.data_page_hex = data_page_hex;
    }

    public byte getCheck() {
        return check;
    }

    public void setCheck(byte check) {
        this.check = check;
    }

    public String getIdentifier_end() {
        return identifier_end;
    }

    public void setIdentifier_end(String identifier_end) {
        this.identifier_end = identifier_end;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "HolloDomain{" +
                "identifier_start='" + identifier_start + '\'' +
                ", functional_ID='" + functional_ID + '\'' +
                ", message_property=" + Arrays.toString(message_property) +
                ", subpackage_flog=" + subpackage_flog +
                ", message_body_encrypt='" + message_body_encrypt + '\'' +
                ", message_body_length=" + message_body_length +
                ", device_ID='" + device_ID + '\'' +
                ", message_number='" + message_number + '\'' +
                ", message_page_total=" + message_page_total +
                ", message_page_number=" + message_page_number +
                ", data_page=" + Arrays.toString(data_page) +
                ", check=" + check +
                ", identifier_end='" + identifier_end + '\'' +
                ", success='" + success + '\'' +
                '}';
    }
}
