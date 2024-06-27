package com.gnluy.wkojsys.model.dto.questionsubmit;

import lombok.Data;

/**
 * @author IGR
 * @version 1.0
 * @description: 判题信息封装类
 * @date 21/6/2024 下午6:30
 */
@Data
public class JudgeInfo {

    /**
     * 时间占用（ms）
     */
    private Long timeLimit;

    /**
     * 内存占用（KB）
     */
    private Long memoryLimit;

    /**
     * 执行信息
     */
    private String message;
}
