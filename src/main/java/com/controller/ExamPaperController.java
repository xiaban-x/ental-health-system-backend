package com.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.ExamPaperEntity;
import com.entity.ExamQuestionEntity;
import com.entity.ExamRecordEntity;
import com.entity.TokenEntity;
import com.entity.dto.AnswerSubmitDTO;
import com.service.ExamPaperService;
import com.service.ExamQuestionService;
import com.service.ExamRecordService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.servlet.http.HttpServletRequest;

/**
 * 试卷表
 * 后端接口
 */
@RestController
@RequestMapping("/api/v1/assessment")
@Tag(name = "试卷管理", description = "试卷的增删改查接口")
public class ExamPaperController {
    @Autowired
    private ExamPaperService examPaperService;

    @Autowired
    private ExamQuestionService examQuestionService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ExamRecordService examRecordService;

    /**
     * 根据当前用户分页查询试卷
     */
    @Operation(summary = "获取我的试卷", description = "获取当前登录用户的试卷分页列表")
    @Parameters({
            @Parameter(name = "page", description = "页码", required = true),
            @Parameter(name = "size", description = "每页数量", required = true),
            @Parameter(name = "title", description = "试卷标题，支持模糊查询")
    })
    @GetMapping("/my-papers")
    public R getMyPapers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String title,
            HttpServletRequest request) {

        // 从请求中获取用户ID
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return R.error("用户未登录");
        }

        QueryWrapper<ExamPaperEntity> queryWrapper = new QueryWrapper<>();
        // 添加用户ID条件
        queryWrapper.eq("user_id", userId);

        // 添加标题模糊查询条件
        if (StringUtils.isNotBlank(title)) {
            queryWrapper.like("title", title);
        }

        // 按创建时间降序排序
        queryWrapper.orderByDesc("created_at");

        Page<ExamPaperEntity> pageResult = examPaperService.page(
                new Page<>(page, size),
                queryWrapper);

        return R.ok().put("data", PageUtils.convert(pageResult));
    }

    /**
     * 查看试卷下的所有题目
     * 
     * @param paperId
     * @return
     */
    @Operation(summary = "查看试卷下的所有题目", description = "根据试卷ID查看试卷下的所有题目")
    @Parameter(name = "paperId", description = "试卷ID", required = true)
    @GetMapping("/{paperId}/questions")
    public R getQuestionsByPaperId(@PathVariable("paperId") Integer paperId) {

        // 创建查询试题的QueryWrapper
        QueryWrapper<ExamQuestionEntity> questionQueryWrapper = new QueryWrapper<>();
        questionQueryWrapper.eq("paper_id", paperId);
        // 获取试卷下的所有试题
        List<ExamQuestionEntity> questionList = examQuestionService.list(questionQueryWrapper);

        return R.ok().put("data", questionList);
    }

    /**
     * 分页查询
     */
    @Operation(summary = "分页查询试卷", description = "根据条件分页查询试卷列表")
    @Parameters({
            @Parameter(name = "page", description = "页码", required = true),
            @Parameter(name = "size", description = "每页数量", required = true),
            @Parameter(name = "name", description = "试卷名称，支持模糊查询")
    })
    @GetMapping("/list")
    public R getExamPapers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name) {
        QueryWrapper<ExamPaperEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);

        Page<ExamPaperEntity> pageResult = examPaperService.page(
                new Page<>(page, size),
                queryWrapper);

        return R.ok().put("data", PageUtils.convert(pageResult));
    }

    /**
     * 获取单个试卷
     */
    @Operation(summary = "获取试卷详情", description = "根据ID获取试卷详细信息")
    @Parameter(name = "id", description = "试卷ID", required = true)
    @GetMapping("/{id}")
    public R getExamPaper(@PathVariable("id") Integer id) {
        ExamPaperEntity examPaper = examPaperService.getById(id);
        if (examPaper == null) {
            return R.error("未找到对应的试卷");
        }
        return R.ok().put("data", examPaper);
    }

    /**
     * 创建试卷
     */
    @Operation(summary = "创建试卷", description = "创建新的试卷")
    @Parameter(name = "examPaper", description = "试卷信息", required = true)
    @PostMapping
    public R createExamPaper(@RequestBody ExamPaperEntity examPaper, HttpServletRequest request) {
        // 从请求中获取用户ID
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return R.error("用户未登录");
        }
        if (examPaper == null || StringUtils.isBlank(examPaper.getTitle())) {
            return R.error("试卷名称不能为空");
        }
        examPaper.setUserId(userId);
        boolean saved = examPaperService.save(examPaper);
        return saved ? R.ok().put("data", examPaper) : R.error("创建失败");
    }

    /**
     * 更新试卷
     */
    @Operation(summary = "更新试卷", description = "更新已有的试卷信息")
    @Parameters({
            @Parameter(name = "id", description = "试卷ID", required = true),
            @Parameter(name = "examPaper", description = "试卷信息", required = true)
    })
    @PutMapping("/{id}")
    public R updateExamPaper(@PathVariable Integer id, @RequestBody ExamPaperEntity examPaper) {
        if (examPaper == null) {
            return R.error("试卷信息不能为空");
        }
        examPaper.setId(id);
        boolean updated = examPaperService.updateById(examPaper);
        return updated ? R.ok().put("data", examPaper) : R.error("更新失败");
    }

    /**
     * 删除试卷
     */
    @Operation(summary = "删除试卷", description = "删除指定的试卷")
    @Parameter(name = "id", description = "试卷ID", required = true)
    @DeleteMapping("/{id}")
    public R deleteExamPaper(@PathVariable Integer id) {
        boolean removed = examPaperService.removeById(id);
        return removed ? R.ok() : R.error("删除失败");
    }

    /**
     * 批量删除试卷
     */
    @Operation(summary = "批量删除试卷", description = "批量删除多个试卷")
    @Parameter(name = "ids", description = "试卷ID数组", required = true)
    @DeleteMapping("/batch")
    public R batchDeleteExamPapers(@RequestBody Integer[] ids) {
        if (ids == null || ids.length == 0) {
            return R.error("删除ID不能为空");
        }
        boolean removed = examPaperService.removeByIds(Arrays.asList(ids));
        return removed ? R.ok() : R.error("批量删除失败");
    }

    /**
     * 提交试卷答案
     */
    @Operation(summary = "提交试卷答案", description = "提交用户的试卷答案")
    @Parameters({
            @Parameter(name = "paperId", description = "试卷ID", required = true),
            @Parameter(name = "answers", description = "答案列表", required = true)
    })
    @PostMapping("/{paperId}/submit")
    public R submitAnswers(
            @PathVariable("paperId") Integer paperId,
            @RequestBody AnswerSubmitDTO submitDTO,
            @RequestHeader("Authorization") String authorization) {

        // 从Authorization头获取token
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return R.error("无效的认证头");
        }
        String token = authorization.substring(7);

        // 获取token中的用户信息
        TokenEntity tokenEntity = tokenService.getTokenEntity(token);
        if (tokenEntity == null) {
            return R.error("token已过期或不存在");
        }

        // 获取试卷信息
        ExamPaperEntity paper = examPaperService.getById(paperId);
        if (paper == null) {
            return R.error("试卷不存在");
        }

        // 计算总分
        int totalScore = 0;

        // 遍历答案计算得分
        for (AnswerSubmitDTO.Answer answer : submitDTO.getAnswers()) {
            // 获取题目信息
            ExamQuestionEntity question = examQuestionService.getById(answer.getQuestionId());
            if (question == null) {
                continue;
            }

            // 判断答案是否正确并计算得分
            if (answer.getOptionValue().equals(question.getAnswer())) {
                totalScore += question.getScore();
            }
        }

        // 创建考试记录
        ExamRecordEntity record = new ExamRecordEntity();
        record.setUserId(tokenEntity.getUserId());
        record.setPaperId(paperId);
        record.setTotalScore(totalScore);

        // 根据得分生成反馈
        String feedback = generateFeedback(totalScore, paper);
        record.setFeedback(feedback);

        // 保存记录
        boolean success = examRecordService.save(record);

        if (!success) {
            return R.error("提交答案失败");
        }

        return R.ok().put("data", record);
    }

    /**
     * 根据得分生成反馈
     * 
     * @param totalScore 总分
     * @param paper      试卷信息
     * @return 反馈内容
     */
    private String generateFeedback(int totalScore, ExamPaperEntity paper) {
        // 这里可以根据试卷类型和得分情况生成不同的反馈
        // 例如心理测试可能有不同的结果解释

        // 简单示例
        if (totalScore >= 80) {
            return "优秀：您的表现非常出色！";
        } else if (totalScore >= 60) {
            return "良好：您的表现不错，还有提升空间。";
        } else {
            return "需要改进：建议您重新学习相关内容。";
        }
    }

    /**
     * 发布问卷
     */
    @Operation(summary = "发布问卷", description = "将问卷状态修改为已发布")
    @Parameter(name = "id", description = "问卷ID", required = true)
    @PostMapping("/{id}/publish")
    public R publishPaper(@PathVariable Integer id) {
        ExamPaperEntity examPaper = examPaperService.getById(id);
        if (examPaper == null) {
            return R.error("问卷不存在");
        }

        // 设置问卷状态为已发布(1)
        examPaper.setStatus(1);
        boolean updated = examPaperService.updateById(examPaper);

        return updated ? R.ok().put("data", examPaper) : R.error("发布失败");
    }

    /**
     * 关闭问卷
     */
    @Operation(summary = "关闭问卷", description = "将问卷状态修改为已关闭")
    @Parameter(name = "id", description = "问卷ID", required = true)
    @PostMapping("/{id}/close")
    public R closePaper(@PathVariable Integer id) {
        ExamPaperEntity examPaper = examPaperService.getById(id);
        if (examPaper == null) {
            return R.error("问卷不存在");
        }

        // 设置问卷状态为已关闭(2)
        examPaper.setStatus(2);
        boolean updated = examPaperService.updateById(examPaper);

        return updated ? R.ok().put("data", examPaper) : R.error("关闭失败");
    }

    /**
     * 交换问题顺序(传回来的序号已经是新的序号了)
     */
    @Operation(summary = "交换问题顺序", description = "交换两个问题的顺序")
    @Parameter(name = "swapRequest", description = "交换顺序请求信息", required = true)
    @PutMapping("/question/swap-sequence")
    public R swapQuestionSequence(@RequestBody Map<String, Object> swapRequest) {
        Integer questionId1 = (Integer) swapRequest.get("questionId1");
        Integer sequence1 = (Integer) swapRequest.get("sequence1");
        Integer questionId2 = (Integer) swapRequest.get("questionId2");
        Integer sequence2 = (Integer) swapRequest.get("sequence2");

        // 参数校验
        if (questionId1 == null || questionId2 == null ||
                sequence1 == null || sequence2 == null) {
            return R.error("参数不完整");
        }

        // 获取两个问题
        ExamQuestionEntity question1 = examQuestionService.getById(questionId1);
        ExamQuestionEntity question2 = examQuestionService.getById(questionId2);

        if (question1 == null || question2 == null) {
            return R.error("问题不存在");
        }

        // 交换序号
        question1.setSequence(sequence1);
        question2.setSequence(sequence2);

        // 批量更新
        boolean success = examQuestionService.updateBatchById(Arrays.asList(question1, question2));

        if (!success) {
            return R.error("更新失败");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("question1", question1);
        result.put("question2", question2);

        return R.ok().put("data", result);
    }
}