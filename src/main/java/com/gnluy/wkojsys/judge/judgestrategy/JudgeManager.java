package com.gnluy.wkojsys.judge.judgestrategy;

import com.gnluy.wkojsys.model.dto.questionsubmit.JudgeInfo;
import com.gnluy.wkojsys.model.entity.Question;
import com.gnluy.wkojsys.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author IGR
 * @version 1.0
 * @description: TODO
 * @date 27/6/2024 下午10:57
 */
@Service
public class JudgeManager {
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeStrategy judgeStrategy = null;
        final QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        final String language = questionSubmit.getLanguage();
        if (language.equals("java")) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        } else {
            judgeStrategy = new DefaultJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
