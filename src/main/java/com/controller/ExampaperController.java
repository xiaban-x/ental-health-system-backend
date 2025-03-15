package com.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.ExampaperEntity;
import com.service.ExampaperService;
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
@RequestMapping("/api/v1/exampapers")  // 改为复数形式，添加版本号
@Tag(name = "试卷管理", description = "试卷的增删改查接口")
public class ExampaperController {
    @Autowired
    private ExampaperService exampaperService;

    /**
     * 分页查询
     */
    @Operation(summary = "分页查询试卷", description = "根据条件分页查询试卷列表")
    @Parameters({
        @Parameter(name = "page", description = "页码", required = true),
        @Parameter(name = "size", description = "每页数量", required = true),
        @Parameter(name = "name", description = "试卷名称，支持模糊查询")
    })
    @GetMapping
    public R getExampapers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name) {
        QueryWrapper<ExampaperEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);

        Page<ExampaperEntity> pageResult = exampaperService.page(
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
    public R getExampaper(@PathVariable("id") Long id) {
        ExampaperEntity exampaper = exampaperService.getById(id);
        if (exampaper == null) {
            return R.error("未找到对应的试卷");
        }
        return R.ok().put("data", exampaper);
    }

    /**
     * 创建试卷
     */
    @Operation(summary = "创建试卷", description = "创建新的试卷")
    @Parameter(name = "exampaper", description = "试卷信息", required = true)
    @PostMapping
    public R createExampaper(@RequestBody ExampaperEntity exampaper) {
        if (exampaper == null || StringUtils.isBlank(exampaper.getName())) {
            return R.error("试卷名称不能为空");
        }
        boolean saved = exampaperService.save(exampaper);
        return saved ? R.ok().put("data", exampaper) : R.error("创建失败");
    }

    /**
     * 更新试卷
     */
    @Operation(summary = "更新试卷", description = "更新已有的试卷信息")
    @Parameters({
        @Parameter(name = "id", description = "试卷ID", required = true),
        @Parameter(name = "exampaper", description = "试卷信息", required = true)
    })
    @PutMapping("/{id}")
    public R updateExampaper(@PathVariable Long id, @RequestBody ExampaperEntity exampaper) {
        if (exampaper == null) {
            return R.error("试卷信息不能为空");
        }
        exampaper.setId(id);
        boolean updated = exampaperService.updateById(exampaper);
        return updated ? R.ok().put("data", exampaper) : R.error("更新失败");
    }

    /**
     * 删除试卷
     */
    @Operation(summary = "删除试卷", description = "删除指定的试卷")
    @Parameter(name = "id", description = "试卷ID", required = true)
    @DeleteMapping("/{id}")
    public R deleteExampaper(@PathVariable Long id) {
        boolean removed = exampaperService.removeById(id);
        return removed ? R.ok() : R.error("删除失败");
    }

    /**
     * 批量删除试卷
     */
    @Operation(summary = "批量删除试卷", description = "批量删除多个试卷")
    @Parameter(name = "ids", description = "试卷ID数组", required = true)
    @DeleteMapping("/batch")
    public R batchDeleteExampapers(@RequestBody Long[] ids) {
        if (ids == null || ids.length == 0) {
            return R.error("删除ID不能为空");
        }
        boolean removed = exampaperService.removeByIds(Arrays.asList(ids));
        return removed ? R.ok() : R.error("批量删除失败");
    }
}
