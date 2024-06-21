package com.gnluy.wkojsys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gnluy.wkojsys.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.gnluy.wkojsys.model.entity.Question;
import com.gnluy.wkojsys.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gnluy.wkojsys.model.entity.User;
import com.gnluy.wkojsys.model.vo.QuestionSubmitVO;

/**
* @author IGR
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2024-06-21 18:19:43
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 提交
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 参数校验
     *
     * @param questionSubmit
     * @return
     */

    void validQuestionSubmit(QuestionSubmit questionSubmit);


    /**
     * 获取题目封装
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 获取题目提交查询条件封装
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 分页获取题目提交
     * @param questionSubmitPage
     * @param loginUser
     * @return
     */

    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);
}
