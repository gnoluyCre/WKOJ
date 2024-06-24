package com.gnluy.wkojsys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gnluy.wkojsys.common.BaseResponse;
import com.gnluy.wkojsys.common.ErrorCode;
import com.gnluy.wkojsys.common.ResultUtils;
import com.gnluy.wkojsys.exception.BusinessException;
import com.gnluy.wkojsys.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.gnluy.wkojsys.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.gnluy.wkojsys.model.entity.QuestionSubmit;
import com.gnluy.wkojsys.model.entity.User;
import com.gnluy.wkojsys.model.enums.QuestionSubmitStatusEnum;
import com.gnluy.wkojsys.model.vo.QuestionSubmitVO;
import com.gnluy.wkojsys.service.QuestionSubmitService;
import com.gnluy.wkojsys.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目提交接口
 *
 * @author <a href="https://github.com/gnoluyCre/WKOJ"></a>
 * @from
 */
@RestController
@RequestMapping("/question_submit")
@Slf4j
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    /**
     * 新增提交记录
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return
     */
    @PostMapping("/do_submit")
    public BaseResponse<Long> doSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
                                       HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能提交
        final User loginUser = userService.getLoginUser(request);
        QuestionSubmit questionSubmit = new QuestionSubmit();
        BeanUtils.copyProperties(questionSubmitAddRequest, questionSubmit);
        questionSubmit.setUserId(loginUser.getId());
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        long result = questionSubmitService.doQuestionSubmit(questionSubmit, loginUser);
        return ResultUtils.success(result);
    }


    @PostMapping("/list/page")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest,
                                                                         HttpServletRequest request) {
        long current = questionSubmitQueryRequest.getCurrent();
        long size = questionSubmitQueryRequest.getPageSize();
        // 从数据库中查询原始的题目提交分页信息
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
                questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
        final User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, loginUser));
    }
}
