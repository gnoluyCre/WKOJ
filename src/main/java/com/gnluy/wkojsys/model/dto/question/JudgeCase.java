package com.gnluy.wkojsys.model.dto.question;

import lombok.Data;

/**
 * @author IGR
 * @version 1.0
 * @description: 封装判题用例的类
 * @date 21/6/2024 下午3:37
 */

@Data
public class JudgeCase {
    /**
     * 输入用例
     */
    private String input;

    /**
     * 输出用例
     */
    private String output;

}
