package com.ryan.userCenter.common;

public class ResultUtils {
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<T>(data, 0, "success");
    }
}
