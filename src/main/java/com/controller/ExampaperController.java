package com.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
     * 查看试卷下的所有题目
     * 
     * @param paperId
     * @return
     */
    @Operation(summary = "查看试卷下的所有题目", description = "根据试卷ID查看试卷下的所有题目")
    @Parameter(name = "paperId", description = "试卷ID", required = true)
    @GetMapping("/{paperId}/questions")
    public R getQuestionsByPaperId(@PathVariable("paperId") Long paperId) {

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
    public R getExamPaper(@PathVariable("id") Long id) {
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
    public R createExamPaper(@RequestBody ExamPaperEntity examPaper) {
        if (examPaper == null || StringUtils.isBlank(examPaper.getTitle())) {
            return R.error("试卷名称不能为空");
        }
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
    public R updateExamPaper(@PathVariable Long id, @RequestBody ExamPaperEntity examPaper) {
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
    public R deleteExamPaper(@PathVariable Long id) {
        boolean removed = examPaperService.removeById(id);
        return removed ? R.ok() : R.error("删除失败");
    }

    /**
     * 批量删除试卷
     */
    @Operation(summary = "批量删除试卷", description = "批量删除多个试卷")
    @Parameter(name = "ids", description = "试卷ID数组", required = true)
    @DeleteMapping("/batch")
    public R batchDeleteExamPapers(@RequestBody Long[] ids) {
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
            @PathVariable("paperId") Long paperId,
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

        // 批量保存答案记录
        List<ExamRecordEntity> records = new ArrayList<>();

        // 修改遍历方式
        for (AnswerSubmitDTO.Answer answer : submitDTO.getAnswers()) {
            // 获取题目信息
            ExamQuestionEntity question = examQuestionService.getById(answer.getQuestionId());
            if (question == null) {
                continue;
            }

            ExamRecordEntity record = new ExamRecordEntity();
            // 设置用户信息(从token中获取)
            record.setUserId(tokenEntity.getUserid());
            record.setUsername(tokenEntity.getUsername());

            record.setPaperId(paperId);
            record.setPaperName(paper.getTitle());
            record.setQuestionId(answer.getQuestionId());
            record.setQuestionName(question.getQuestionName());
            record.setOptions(question.getOptions());
            record.setScore(question.getScore());
            record.setAnswer(question.getAnswer());
            record.setAnalysis(question.getAnalysis());
            record.setUserAnswer(answer.getOptionValue());

            // 判断答案是否正确并计算得分
            if (answer.getOptionValue().equals(question.getAnswer())) {
                record.setUserScore(question.getScore());
            } else {
                record.setUserScore(0L);
            }

            records.add(record);
        }

        // 批量保存记录
        boolean success = examRecordService.saveBatch(records);

        if (!success) {
            return R.error("提交答案失败");
        }

        return R.ok().put("data", records);
    }
}