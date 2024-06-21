package com.gnluy.wkojsys.model.dto.question;

import lombok.Data;

/**
 * @author IGR
 * @version 1.0
 * @description: 封装判题配置（题目限制）类
 * @date 21/6/2024 下午3:37
 */

@Data
public class JudgeConfig {

    /**
     * 时间限制（ms）
     */
    private String timeLimit;

    /**
     * 内存限制（KB）
     */
    private String memoryLimit;

    /**
     * 堆栈限制（KB）
     */
    private String stackLimit;
}
