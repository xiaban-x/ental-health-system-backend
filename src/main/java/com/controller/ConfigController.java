package com.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.ConfigEntity;
import com.service.ConfigService;
import com.utils.PageUtils;
import com.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 配置管理接口
 * 
 * @author xiaban
 * @version 1.0
 */
@RestController
@RequestMapping("/config")
@Tag(name = "配置管理", description = "系统配置管理相关接口")
public class ConfigController {
    @Autowired
    private ConfigService configService;

    @Operation(summary = "分页查询配置", description = "获取配置信息的分页列表")
    @Parameters({
            @Parameter(name = "params", description = "分页参数", required = true),
            @Parameter(name = "config", description = "配置查询条件")
    })
    @GetMapping("/page")
    public R page(@RequestParam Map<String, Object> params, ConfigEntity config) {
        PageUtils page = configService.queryPage(params);
        return R.ok().put("data", page);
    }

    @Operation(summary = "获取配置列表", description = "获取所有配置信息")
    @Parameters({
            @Parameter(name = "params", description = "查询参数"),
            @Parameter(name = "config", description = "配置查询条件")
    })
    @IgnoreAuth
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params, ConfigEntity config) {
        PageUtils page = configService.queryPage(params);
        return R.ok().put("data", page);
    }

    /**
     * 获取配置信息
     */
    @Operation(summary = "获取配置信息", description = "根据ID获取单个配置的详细信息")
    @Parameter(name = "id", description = "配置ID", required = true)
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") String id) {
        ConfigEntity config = configService.getById(id);
        if (config == null) {
            return R.error("未找到对应的配置");
        }
        return R.ok().put("data", config);
    }

    /**
     * 获取配置详情
     */
    @Operation(summary = "获取配置详情", description = "根据ID获取配置详情，无需认证")
    @Parameter(name = "id", description = "配置ID", required = true)
    @IgnoreAuth
    @GetMapping("/detail/{id}")
    public R detail(@PathVariable("id") String id) {
        ConfigEntity config = configService.getById(id);
        if (config == null) {
            return R.error("未找到对应的配置详情");
        }
        return R.ok().put("data", config);
    }

    /**
     * 根据 name 获取配置信息
     */
    @Operation(summary = "根据名称获取配置", description = "根据配置名称获取配置信息")
    @Parameter(name = "name", description = "配置名称", required = true)
    @GetMapping("/info")
    public R infoByName(@RequestParam String name) {
        if (StringUtils.isBlank(name)) {
            return R.error("参数 name 不能为空");
        }
        ConfigEntity config = configService.getOne(new QueryWrapper<ConfigEntity>().eq("name", name), false);
        if (config == null) {
            return R.error("未找到 name 对应的配置");
        }
        return R.ok().put("data", config);
    }

    /**
     * 新增配置
     */
    @Operation(summary = "新增配置", description = "创建新的配置项")
    @Parameter(name = "config", description = "配置信息", required = true)
    @PostMapping("/save")
    public R save(@RequestBody ConfigEntity config) {
        if (config == null || StringUtils.isBlank(config.getName())) {
            return R.error("配置名称不能为空");
        }
        boolean saved = configService.save(config);
        return saved ? R.ok() : R.error("保存失败");
    }

    /**
     * 修改配置
     */
    @Operation(summary = "修改配置", description = "更新已有的配置信息")
    @Parameter(name = "config", description = "配置信息", required = true)
    @PutMapping("/update")
    public R update(@RequestBody ConfigEntity config) {
        if (config == null || config.getId() == null) {
            return R.error("ID 不能为空");
        }
        boolean updated = configService.updateById(config);
        return updated ? R.ok() : R.error("更新失败");
    }

    /**
     * 删除配置
     */
    @Operation(summary = "删除配置", description = "批量删除配置信息")
    @Parameter(name = "ids", description = "配置ID数组", required = true)
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        if (ids == null || ids.length == 0) {
            return R.error("删除 ID 不能为空");
        }
        boolean removed = configService.removeByIds(Arrays.asList(ids));
        return removed ? R.ok() : R.error("删除失败");
    }
}
