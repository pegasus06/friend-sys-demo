package com.ryan.userCenter.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseResponse<T> implements Serializable {
    private T data;
    private int code;
    private String message;
    private String description;

    public BaseResponse(T data, int code, String message, String description) {
        this.data = data;
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(T data, int code, String message) {
        this(data, code, message, "");
    }

    public BaseResponse(T data, int code) {
        this(data, code, "", "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(null, errorCode.getCode(), errorCode.getMsg(), errorCode.getDescription());
    }
}
