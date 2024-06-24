package com.gnluy.wkojsys.judge.codesandbox.model;

import com.gnluy.wkojsys.model.dto.questionsubmit.JudgeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.PipedReader;
import java.util.List;

/**
 * @author IGR
 * @version 1.0
 * @description: TODO
 * @date 22/6/2024 下午8:41
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeResponse {

    /**
     * 输出结果
     */
    private List<String> outputList;

    /**
     * 接口信息
     */
    private String message;

    /**
     * 执行状态
     */
    private Integer status;

    /**
     * 判题信息
     */
    private JudgeInfo judgeInfo;
}
