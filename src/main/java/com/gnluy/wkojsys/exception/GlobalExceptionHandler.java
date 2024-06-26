package com.gnluy.wkojsys.exception;

import com.gnluy.wkojsys.common.BaseResponse;
import com.gnluy.wkojsys.common.ErrorCode;
import com.gnluy.wkojsys.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *@RestControllerAdvice注解是@ControllerAdvie和@ResponseBody注解的组合，先捕获整个应用程序中抛出的异常，然后将异常处理方法的返回值将自动转换为HTTP响应的主体。
 * @ExceptionHandler注解用于指定什么异常需要被捕获。
 * @author <a href="https://github.com/gnoluyCre/WKOJ"></a>
 * @from 
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException", e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }
}
