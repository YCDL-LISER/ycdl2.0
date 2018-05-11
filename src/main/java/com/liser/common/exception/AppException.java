package com.liser.common.exception;

/**
 * 自定义异常类
 */
public class AppException extends RuntimeException {

    private String errorMessage;
    private String fieldName;

    AppException() {
    }

    public AppException(String msg) {
        super(msg);
        errorMessage = msg;
    }

    public AppException(String msg, String fieldName) {
        super(msg);
        errorMessage = msg;
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getMessage() {
        return errorMessage;
    }

}
