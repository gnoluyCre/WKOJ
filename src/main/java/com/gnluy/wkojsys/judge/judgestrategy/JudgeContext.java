package com.gnluy.wkojsys.judge.judgestrategy;

import com.gnluy.wkojsys.model.dto.questionsubmit.JudgeInfo;
import com.gnluy.wkojsys.model.entity.Question;
import com.gnluy.wkojsys.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * @author IGR
 * @version 1.0
 * @description: TODO
 * @date 27/6/2024 下午10:34
 */
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> outputList;

    private Question question;

    private QuestionSubmit questionSubmit;
}
