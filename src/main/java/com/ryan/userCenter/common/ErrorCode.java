package com.ryan.userCenter.common;

import lombok.Data;
import lombok.Getter;

@Getter
public enum ErrorCode {
    SUCCESS(200, "成功", ""),
    FAIL(500, "失败", ""),
    PARAMS_ERROR(400, "参数错误", ""),
    USER_NOT_EXIST(404, "用户不存在", ""),
    USER_PASSWORD_ERROR(405, "用户密码错误", ""),
    USER_ALREADY_EXIST(406, "用户已存在", ""),
    USER_NOT_LOGIN(407, "用户未登录", ""),
    System_ERROR(408, "系统错误", "");
    private final int code;
    private final String msg;
    private final String description;

    ErrorCode(int code, String msg, String description) {
        this.code = code;
        this.msg = msg;
        this.description = description;
    }

}
