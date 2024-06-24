package com.gnluy.wkojsys.judge.codesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class ExecuteCodeRequest {

    /**
     * 输入用例
     */
    private List<String> inputList;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 编程语言
     */
    private String language;
}
