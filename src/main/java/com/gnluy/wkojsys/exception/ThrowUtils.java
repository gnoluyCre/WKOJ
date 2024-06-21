package com.gnluy.wkojsys.exception;

import com.gnluy.wkojsys.common.ErrorCode;

/**
 * 抛异常工具类
 *一般用于请求参数的校验，如果请求参数为空，直接抛出业务异常，然后指明错误码ErroCode和message错误信息。
 * @author <a href="https://github.com/gnoluyCre/WKOJ"></a>
 * @from 
 */
public class ThrowUtils {

    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param runtimeException
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param errorCode
     */
    public static void throwIf(boolean condition, ErrorCode errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param errorCode
     * @param message
     */
    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }
}
