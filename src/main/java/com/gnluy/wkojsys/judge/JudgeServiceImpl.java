package com.gnluy.wkojsys.judge;

import cn.hutool.json.JSONUtil;
import com.gnluy.wkojsys.common.ErrorCode;
import com.gnluy.wkojsys.exception.BusinessException;
import com.gnluy.wkojsys.judge.codesandbox.CodeSandBox;
import com.gnluy.wkojsys.judge.codesandbox.CodeSandBoxFactory;
import com.gnluy.wkojsys.judge.codesandbox.CodeSandBoxProxy;
import com.gnluy.wkojsys.judge.codesandbox.model.ExecuteCodeRequest;
import com.gnluy.wkojsys.judge.codesandbox.model.ExecuteCodeResponse;
import com.gnluy.wkojsys.judge.judgestrategy.JudgeContext;
import com.gnluy.wkojsys.judge.judgestrategy.JudgeManager;
import com.gnluy.wkojsys.model.dto.questionsubmit.JudgeInfo;
import com.gnluy.wkojsys.model.entity.Question;
import com.gnluy.wkojsys.model.entity.QuestionSubmit;
import com.gnluy.wkojsys.model.enums.QuestionSubmitLanguageEnum;
import com.gnluy.wkojsys.model.enums.QuestionSubmitStatusEnum;
import com.gnluy.wkojsys.service.QuestionService;
import com.gnluy.wkojsys.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author IGR
 * @version 1.0
 * @description: 判题服务实现类
 * @date 24/6/2024 下午1:39
 */

@Service
// todo 整个方法加锁
public class JudgeServiceImpl implements JudgeService {
    @Resource
    QuestionService questionService;

    @Resource
    QuestionSubmitService questionSubmitService;

    @Value("${codesandbox.type:example}")
    private String type;

    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        //1）传入题目的提交 id，获取到对应的题目、提交信息（包含代码、编程语言等）
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目提交信息不存在");
        }
        long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目不存在，操作失败");
        }

        // 2）如果题目提交状态不为等待中，就不用重复执行了
        if (!QuestionSubmitStatusEnum.WAITING.getValue().equals(questionSubmit.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "已经在判题中无需重复提交");
        }

        //3）更改判题（题目提交）的状态为 “判题中”，防止重复执行，也能让用户即时看到状态
        QuestionSubmit questionSubmit1 = new QuestionSubmit();
        questionSubmit1.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit1.setId(questionSubmitId);
        final boolean update = questionSubmitService.updateById(questionSubmit1);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统错误");
        }

        //4）调用沙箱，获取到执行结果
        CodeSandBox codeSandBox = CodeSandBoxFactory.newInstance(type);
        codeSandBox = new CodeSandBoxProxy(codeSandBox);
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder().
                code("int main {}").
                language(QuestionSubmitLanguageEnum.JAVA.getValue()).
                inputList(new ArrayList<>(Arrays.asList(new String[]{"a", "b"}))).
                build();
        final ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);

        //5）根据沙箱的执行结果，设置题目的判题状态和信息
        List<String> outputList = executeCodeResponse.getOutputList();
        JudgeInfo judgeInfo = executeCodeResponse.getJudgeInfo();

        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(judgeInfo);
        judgeContext.setOutputList(outputList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);

        JudgeManager judgeManager = new JudgeManager();
        JudgeInfo judgeInfoResult = judgeManager.doJudge(judgeContext);
        QuestionSubmit questionSubmitResult = new QuestionSubmit();
        questionSubmitResult.setId(questionSubmitId);
        questionSubmitResult.setJudgeInfo(JSONUtil.toJsonStr(judgeInfoResult));
        questionSubmitResult.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());

        final boolean updateQuestionSubmit = questionSubmitService.updateById(questionSubmitResult);
        if (!updateQuestionSubmit) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统错误");
        }
        final QuestionSubmit result = questionSubmitService.getById(questionSubmitId);
        return result;
    }
}
