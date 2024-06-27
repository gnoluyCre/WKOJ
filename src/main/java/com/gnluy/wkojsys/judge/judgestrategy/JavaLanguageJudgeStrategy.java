package com.gnluy.wkojsys.judge.judgestrategy;

import cn.hutool.json.JSONUtil;
import com.gnluy.wkojsys.model.dto.question.JudgeCase;
import com.gnluy.wkojsys.model.dto.question.JudgeConfig;
import com.gnluy.wkojsys.model.dto.questionsubmit.JudgeInfo;
import com.gnluy.wkojsys.model.entity.Question;
import com.gnluy.wkojsys.model.enums.JudgeInfoMessageEnum;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author IGR
 * @version 1.0
 * @description: TODO
 * @date 27/6/2024 下午10:35
 */
public class JavaLanguageJudgeStrategy implements JudgeStrategy {
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        Long timeLimit = judgeInfo.getTimeLimit();
        Long memoryLimit = judgeInfo.getMemoryLimit();
        List<String> outputList = judgeContext.getOutputList();
        Question question = judgeContext.getQuestion();
        String judgeCase = question.getJudgeCase();

        //1.先判断沙箱执行的结果输出数量是否和预期输出数量相等
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCase, JudgeCase.class);
        List<String> judgeCaseOutputList = judgeCaseList.stream().map(JudgeCase::getOutput).collect(Collectors.toList());
        JudgeInfo judgeInfoResult = new JudgeInfo();
        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
        judgeInfoResult.setMessage(judgeInfoMessageEnum.getValue());
        judgeInfoResult.setTimeLimit(timeLimit);
        judgeInfoResult.setMemoryLimit(memoryLimit);

        if (judgeCaseOutputList.size() != outputList.size()) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoResult.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResult;
        }

        //2.依次判断每一项输出和预期输出是否相等
        for (int i = 0; i < judgeCaseOutputList.size(); i++) {
            final boolean equals = judgeCaseOutputList.get(i).equals(outputList.get(i));
            if (!equals) {
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfoResult.setMessage(judgeInfoMessageEnum.getValue());
                return judgeInfoResult;
            }
        }

        //3.判题题目的限制是否符合要求
        String exceptedJudgeConfigStr = question.getJudgeConfig();
        JudgeConfig exceptedJudgeConfig = JSONUtil.toBean(exceptedJudgeConfigStr, JudgeConfig.class);
        long extTimeLimit = exceptedJudgeConfig.getTimeLimit();
        long exMemoryLimit = exceptedJudgeConfig.getMemoryLimit();
        final Long JAVA_PROGRAM_TIME = 10000L;

        if (memoryLimit - JAVA_PROGRAM_TIME > exMemoryLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResult.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResult;
        }
        if (timeLimit > extTimeLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResult.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResult;
        }

        //4.可能还有其他的异常情况
        return judgeInfoResult;
    }
}
