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

/**
 * 试卷表
 * 后端接口
 */
@RestController
@RequestMapping("/exampaper")
public class ExampaperController {

    @Autowired
    private ExampaperService exampaperService;

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public R page(@RequestParam Map<String, Object> params, ExampaperEntity exampaper) {
        QueryWrapper<ExampaperEntity> queryWrapper = new QueryWrapper<>();
        // 构建查询条件
        queryWrapper.like(StringUtils.isNotBlank(exampaper.getName()), "name", exampaper.getName());

        // 分页查询
        Page<ExampaperEntity> page = exampaperService.page(
                new Page<>(Long.valueOf(params.get("page").toString()), Long.valueOf(params.get("limit").toString())),
                queryWrapper);

        return R.ok().put("data", PageUtils.convert(page));
    }

    /**
     * 获取列表
     */
    @IgnoreAuth
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params, ExampaperEntity exampaper) {
        QueryWrapper<ExampaperEntity> queryWrapper = new QueryWrapper<>();
        // 构建查询条件
        queryWrapper.like(StringUtils.isNotBlank(exampaper.getName()), "name", exampaper.getName());

        // 获取列表
        return R.ok().put("data", exampaperService.list(queryWrapper));
    }

    /**
     * 获取配置信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        ExampaperEntity exampaper = exampaperService.getById(id);
        if (exampaper == null) {
            return R.error("未找到对应的试卷");
        }
        return R.ok().put("data", exampaper);
    }

    /**
     * 新增试卷
     */
    @PostMapping("/save")
    public R save(@RequestBody ExampaperEntity exampaper) {
        if (exampaper == null || StringUtils.isBlank(exampaper.getName())) {
            return R.error("试卷名称不能为空");
        }
        boolean saved = exampaperService.save(exampaper);
        return saved ? R.ok() : R.error("保存失败");
    }

    /**
     * 修改试卷
     */
    @PutMapping("/update")
    public R update(@RequestBody ExampaperEntity exampaper) {
        if (exampaper == null || exampaper.getId() == null) {
            return R.error("ID 不能为空");
        }
        boolean updated = exampaperService.updateById(exampaper);
        return updated ? R.ok() : R.error("更新失败");
    }

    /**
     * 删除试卷
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        if (ids == null || ids.length == 0) {
            return R.error("删除 ID 不能为空");
        }
        boolean removed = exampaperService.removeByIds(Arrays.asList(ids));
        return removed ? R.ok() : R.error("删除失败");
    }
}
