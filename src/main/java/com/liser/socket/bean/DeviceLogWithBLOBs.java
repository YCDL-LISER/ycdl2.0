package com.liser.socket.bean;

public class DeviceLogWithBLOBs extends DeviceLog {
    private String content;

    private String original;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original == null ? null : original.trim();
    }
}