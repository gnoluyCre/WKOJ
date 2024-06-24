package com.gnluy.wkojsys.judge.codesandbox.impl;

import com.gnluy.wkojsys.judge.codesandbox.CodeSandBox;
import com.gnluy.wkojsys.judge.codesandbox.model.ExecuteCodeRequest;
import com.gnluy.wkojsys.judge.codesandbox.model.ExecuteCodeResponse;
import com.gnluy.wkojsys.model.dto.questionsubmit.JudgeInfo;

import java.util.List;

/**
 * @author IGR
 * @version 1.0
 * @description: 测试流程代码沙箱
 * @date 22/6/2024 下午9:03
 */
public class ExampleCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("示例代码沙箱流程测试");
        executeCodeResponse.setStatus(1);
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMemoryLimit("10000");
        judgeInfo.setStackLimit("10000");
        judgeInfo.setTimeLimit("10000");
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
