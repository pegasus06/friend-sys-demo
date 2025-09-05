package com.ryan.userCenter.common;

public class ResultUtils {
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(data, 0, "success");
    }

    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    public static <T> BaseResponse<T> error(int code, String message, String description) {
        return new BaseResponse<>(null, code, message, description);
    }

    public static <T> BaseResponse<T> error(ErrorCode errorCode, String description) {
        return new BaseResponse<>(null, errorCode.getCode(), errorCode.getMsg(), description);
    }

    public static <T> BaseResponse<T> error(ErrorCode errorCode, String description, String msg) {
        return new BaseResponse<>(null, errorCode.getCode(), msg, description);
    }

}
