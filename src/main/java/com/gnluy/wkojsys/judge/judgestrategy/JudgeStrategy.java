package com.gnluy.wkojsys.judge.judgestrategy;


import com.gnluy.wkojsys.model.dto.questionsubmit.JudgeInfo;

public interface JudgeStrategy {
    JudgeInfo doJudge(JudgeContext judgeContext);
}
