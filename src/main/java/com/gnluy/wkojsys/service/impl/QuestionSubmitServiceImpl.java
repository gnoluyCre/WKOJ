package com.gnluy.wkojsys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gnluy.wkojsys.common.ErrorCode;
import com.gnluy.wkojsys.constant.CommonConstant;
import com.gnluy.wkojsys.exception.BusinessException;
import com.gnluy.wkojsys.judge.JudgeService;
import com.gnluy.wkojsys.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.gnluy.wkojsys.model.entity.QuestionSubmit;
import com.gnluy.wkojsys.model.entity.User;
import com.gnluy.wkojsys.model.enums.QuestionSubmitLanguageEnum;
import com.gnluy.wkojsys.model.enums.QuestionSubmitStatusEnum;
import com.gnluy.wkojsys.model.vo.QuestionSubmitVO;
import com.gnluy.wkojsys.service.QuestionService;
import com.gnluy.wkojsys.service.QuestionSubmitService;
import com.gnluy.wkojsys.mapper.QuestionSubmitMapper;
import com.gnluy.wkojsys.service.UserService;
import com.gnluy.wkojsys.utils.SqlUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author IGR
 * @description 针对表【question_submit(题目提交)】的数据库操作Service实现
 * @createDate 2024-06-21 18:19:43
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    // todo 循环依赖
    @Resource
    @Lazy
    private JudgeService judgeService;

    @Override
    public long doQuestionSubmit(QuestionSubmit questionSubmit, User loginUser) {
        questionSubmit.setJudgeInfo("{}");
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        this.validQuestionSubmit(questionSubmit);

        // todo  加锁 用户串行提交题目
        QuestionSubmit questionSubmit1 = new QuestionSubmit();
        questionSubmit1.setLanguage(questionSubmit.getLanguage());
        questionSubmit1.setCode(questionSubmit.getCode());
        questionSubmit1.setJudgeInfo(questionSubmit.getJudgeInfo());
        questionSubmit1.setStatus(questionSubmit.getStatus());
        questionSubmit1.setQuestionId(questionSubmit.getQuestionId());
        questionSubmit1.setUserId(loginUser.getId());
        
        final boolean save = this.save(questionSubmit1);
        if (!save) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "提交失败");
        }
        Long questionSubmitId = questionSubmit1.getId();

        //异步执行判题服务
        CompletableFuture.runAsync(() -> {
            judgeService.doJudge(questionSubmitId);
        });
        return questionSubmitId;
    }

    @Override
    public void validQuestionSubmit(QuestionSubmit questionSubmit) {
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        Integer status = questionSubmit.getStatus();
        Long questionId = questionSubmit.getQuestionId();

        if (StringUtils.isBlank(code) || code.length() >= 4096) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户代码为空或过长");
        }
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }
        //判断题目是否存在,不存在则抛NOT_FOUND_ERROR异常
        if (questionService.getById(questionId) == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
    }

    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        //转化为stream对象再对每一个元素做getQuestionSubmitVO的操作再collect转化为list
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream()
                .map(questionSubmit -> getQuestionSubmitVO(questionSubmit, loginUser))
                .collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        // 脱敏：仅本人和管理员能看见自己（提交 userId 和登录用户 id 不同）提交的代码
        long userId = loginUser.getId();
        // 处理脱敏
        if (userId != questionSubmit.getUserId() && !userService.isAdmin(loginUser)) {
            questionSubmitVO.setCode(null);
        }
        return questionSubmitVO;
    }
}





