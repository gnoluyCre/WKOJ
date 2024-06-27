package com.gnluy.wkojsys.judge;

import com.gnluy.wkojsys.model.entity.QuestionSubmit;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

public interface JudgeService {
    QuestionSubmit doJudge(long questionSubmitId);
}
