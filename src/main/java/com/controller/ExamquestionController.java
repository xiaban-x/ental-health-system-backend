package com.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.ExamquestionEntity;
import com.entity.view.ExamquestionView;
import com.service.ExamquestionService;
import com.utils.PageUtils;
import com.utils.R;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 试题表 后端接口
 */
@RestController
@RequestMapping("/api/v1/questions")
@Tag(name = "试题管理", description = "试题的增删改查接口")
public class ExamquestionController {
    @Autowired
    private ExamquestionService examquestionService;

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
            ExamquestionEntity examquestion) {
        QueryWrapper<ExamquestionEntity> queryWrapper = new QueryWrapper<>();
        // 添加查询条件
        if (examquestion != null) {
            // 根据实际字段添加条件
        }
        Page<ExamquestionEntity> pageResult = examquestionService.page(
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
    public R getQuestion(@PathVariable("id") Long id) {
        ExamquestionEntity examquestion = examquestionService.getById(id);
        if (examquestion == null) {
            return R.error("试题不存在");
        }
        return R.ok().put("data", examquestion);
    }

    /**
     * 创建试题
     */
    @Operation(summary = "创建试题", description = "创建新的试题")
    @Parameter(name = "examquestion", description = "试题信息", required = true)
    @PostMapping
    public R createQuestion(@RequestBody ExamquestionEntity examquestion) {
        // 使用雪花算法或其他更可靠的ID生成方式
        examquestion.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        boolean saved = examquestionService.save(examquestion);
        return saved ? R.ok().put("data", examquestion) : R.error("创建失败");
    }

    /**
     * 更新试题
     */
    @Operation(summary = "更新试题", description = "更新已有的试题信息")
    @Parameters({
        @Parameter(name = "id", description = "试题ID", required = true),
        @Parameter(name = "examquestion", description = "试题信息", required = true)
    })
    @PutMapping("/{id}")
    public R updateQuestion(@PathVariable Long id, @RequestBody ExamquestionEntity examquestion) {
        examquestion.setId(id);
        boolean updated = examquestionService.updateById(examquestion);
        return updated ? R.ok().put("data", examquestion) : R.error("更新失败");
    }

    /**
     * 删除试题
     */
    @Operation(summary = "删除试题", description = "删除指定的试题")
    @Parameter(name = "id", description = "试题ID", required = true)
    @DeleteMapping("/{id}")
    public R deleteQuestion(@PathVariable Long id) {
        boolean removed = examquestionService.removeById(id);
        return removed ? R.ok() : R.error("删除失败");
    }

    /**
     * 批量删除试题
     */
    @Operation(summary = "批量删除试题", description = "批量删除多个试题")
    @Parameter(name = "ids", description = "试题ID数组", required = true)
    @DeleteMapping("/batch")
    public R batchDeleteQuestions(@RequestBody Long[] ids) {
        if (ids == null || ids.length == 0) {
            return R.error("删除ID不能为空");
        }
        boolean removed = examquestionService.removeByIds(Arrays.asList(ids));
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

        QueryWrapper<ExamquestionEntity> wrapper = new QueryWrapper<>();
        if (params.get("remindstart") != null) {
            wrapper.ge(field, params.get("remindstart"));
        }
        if (params.get("remindend") != null) {
            wrapper.le(field, params.get("remindend"));
        }

        int count = (int) examquestionService.count(wrapper);
        return R.ok().put("count", count);
    }
}
