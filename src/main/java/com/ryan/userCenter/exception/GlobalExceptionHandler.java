package com.ryan.userCenter.exception;

import com.ryan.userCenter.common.BaseResponse;
import com.ryan.userCenter.common.ErrorCode;
import com.ryan.userCenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
//全局异常处理
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public <T> BaseResponse<T> businessExceptionHandler(BusinessException e) {
        log.error("业务异常：{}", e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public <T> BaseResponse<T> runtimeExceptionHandler(Exception e) {
        log.error("运行时异常：{}", e.getMessage(), e);
        return ResultUtils.error(ErrorCode.System_ERROR);
    }
}
