package com.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.ExamQuestionEntity;
import com.service.ExamQuestionService;
import com.utils.PageUtils;
import com.utils.R;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.apache.commons.lang3.StringUtils;
import java.util.Date;

/**
 * 试题表 后端接口
 */
@RestController
@RequestMapping("/api/v1/questions")
@Tag(name = "试题管理", description = "试题的增删改查接口")
public class ExamQuestionController {
    @Autowired
    private ExamQuestionService examQuestionService;

    /**
     * 分页查询试题
     */
    @Operation(summary = "分页查询试题", description = "获取试题的分页列表")
    @Parameters({
            @Parameter(name = "page", description = "页码", required = true),
            @Parameter(name = "size", description = "每页数量", required = true),
            @Parameter(name = "name", description = "试题名称"),
            @Parameter(name = "type", description = "试题类型")
    })
    @GetMapping
    public R getQuestions(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            ExamQuestionEntity examQuestion) {
        QueryWrapper<ExamQuestionEntity> queryWrapper = new QueryWrapper<>();
        // 添加查询条件
        if (examQuestion != null) {
            // 根据实际字段添加条件
        }
        Page<ExamQuestionEntity> pageResult = examQuestionService.page(
                new Page<>(page, size),
                queryWrapper);
        return R.ok().put("data", new PageUtils(pageResult));
    }

    /**
     * 获取单个试题
     */
    @Operation(summary = "获取试题详情", description = "根据ID获取试题详细信息")
    @Parameter(name = "id", description = "试题ID", required = true)
    @GetMapping("/{id}")
    public R getQuestion(@PathVariable("id") Integer id) {
        ExamQuestionEntity examQuestion = examQuestionService.getById(id);
        if (examQuestion == null) {
            return R.error("试题不存在");
        }
        return R.ok().put("data", examQuestion);
    }

    /**
     * 创建试题
     */
    @Operation(summary = "创建试题", description = "创建新的试题")
    @Parameter(name = "examQuestion", description = "试题信息", required = true)
    @PostMapping
    public R createQuestion(@RequestBody ExamQuestionEntity examQuestion) {
        // 检查必要字段
        if (examQuestion == null || StringUtils.isBlank(examQuestion.getQuestionName())) {
            return R.error("试题信息不能为空");
        }

        // 设置创建时间和更新时间
        Date now = new Date();
        examQuestion.setCreatedAt(now);
        examQuestion.setUpdatedAt(now);

        // 检查序号是否存在
        if (examQuestion.getSequence() != null && examQuestion.getPaperId() != null) {
            QueryWrapper<ExamQuestionEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("paper_id", examQuestion.getPaperId())
                    .eq("sequence", examQuestion.getSequence());
            
            // 检查同一试卷下是否存在相同序号
            if (examQuestionService.count(queryWrapper) > 0) {
                // 获取当前试卷下的最大序号
                queryWrapper.clear();
                queryWrapper.eq("paper_id", examQuestion.getPaperId())
                        .orderByDesc("sequence")
                        .last("LIMIT 1");
                
                ExamQuestionEntity maxSequenceQuestion = examQuestionService.getOne(queryWrapper);
                int newSequence = maxSequenceQuestion != null ? maxSequenceQuestion.getSequence() + 1 : 1;
                examQuestion.setSequence(newSequence);
            }
        } else if (examQuestion.getPaperId() != null) {
            // 如果未指定序号，自动设置为当前试卷最大序号+1
            QueryWrapper<ExamQuestionEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("paper_id", examQuestion.getPaperId())
                    .orderByDesc("sequence")
                    .last("LIMIT 1");
            
            ExamQuestionEntity maxSequenceQuestion = examQuestionService.getOne(queryWrapper);
            int newSequence = maxSequenceQuestion != null ? maxSequenceQuestion.getSequence() + 1 : 1;
            examQuestion.setSequence(newSequence);
        }

        // 使用更可靠的ID生成方式
        examQuestion.setId((int) IdWorker.getId());

        try {
            boolean saved = examQuestionService.save(examQuestion);
            return saved ? R.ok().put("data", examQuestion) : R.error("创建失败");
        } catch (Exception e) {
            return R.error("创建试题失败：" + e.getMessage());
        }
    }

    /**
     * 更新试题
     */
    @Operation(summary = "更新试题", description = "更新已有的试题信息")
    @Parameters({
            @Parameter(name = "id", description = "试题ID", required = true),
            @Parameter(name = "examQuestion", description = "试题信息", required = true)
    })
    @PutMapping("/{id}")
    public R updateQuestion(@PathVariable Integer id, @RequestBody ExamQuestionEntity examQuestion) {
        examQuestion.setId(id);
        boolean updated = examQuestionService.updateById(examQuestion);
        return updated ? R.ok().put("data", examQuestion) : R.error("更新失败");
    }

    /**
     * 删除试题
     */
    @Operation(summary = "删除试题", description = "删除指定的试题")
    @Parameter(name = "id", description = "试题ID", required = true)
    @DeleteMapping("/{id}")
    public R deleteQuestion(@PathVariable Integer id) {
        boolean removed = examQuestionService.removeById(id);
        return removed ? R.ok() : R.error("删除失败");
    }

    /**
     * 批量删除试题
     */
    @Operation(summary = "批量删除试题", description = "批量删除多个试题")
    @Parameter(name = "ids", description = "试题ID数组", required = true)
    @DeleteMapping("/batch")
    public R batchDeleteQuestions(@RequestBody Integer[] ids) {
        if (ids == null || ids.length == 0) {
            return R.error("删除ID不能为空");
        }
        boolean removed = examQuestionService.removeByIds(Arrays.asList(ids));
        return removed ? R.ok() : R.error("批量删除失败");
    }

    /**
     * 获取提醒数量
     */
    @Operation(summary = "获取提醒数量", description = "获取指定条件的提醒记录数量")
    @Parameters({
            @Parameter(name = "field", description = "字段名", required = true),
            @Parameter(name = "type", description = "类型(1:数字 2:日期)", required = true),
            @Parameter(name = "startDays", description = "开始提醒天数"),
            @Parameter(name = "endDays", description = "结束提醒天数")
    })
    @GetMapping("/reminders/{field}/{type}")
    public R getRemindersCount(
            @PathVariable("field") String field,
            @PathVariable("type") String type,
            @RequestParam(required = false) Integer startDays,
            @RequestParam(required = false) Integer endDays) {

        Map<String, Object> params = new HashMap<>();
        params.put("column", field);
        params.put("type", type);
        params.put("remindstart", startDays);
        params.put("remindend", endDays);

        // 日期类型处理
        if ("2".equals(type)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();

            if (startDays != null) {
                c.setTime(new Date());
                c.add(Calendar.DAY_OF_MONTH, startDays);
                params.put("remindstart", sdf.format(c.getTime()));
            }

            if (endDays != null) {
                c.setTime(new Date());
                c.add(Calendar.DAY_OF_MONTH, endDays);
                params.put("remindend", sdf.format(c.getTime()));
            }
        }

        QueryWrapper<ExamQuestionEntity> wrapper = new QueryWrapper<>();
        if (params.get("remindstart") != null) {
            wrapper.ge(field, params.get("remindstart"));
        }
        if (params.get("remindend") != null) {
            wrapper.le(field, params.get("remindend"));
        }

        int count = (int) examQuestionService.count(wrapper);
        return R.ok().put("count", count);
    }
}
