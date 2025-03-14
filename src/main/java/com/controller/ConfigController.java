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

/**
 * 配置管理接口
 */
@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public R page(@RequestParam Map<String, Object> params, ConfigEntity config) {
        PageUtils page = configService.queryPage(params);
        return R.ok().put("data", page);
    }

    /**
     * 获取列表
     */
    @IgnoreAuth
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params, ConfigEntity config) {
        PageUtils page = configService.queryPage(params);
        return R.ok().put("data", page);
    }

    /**
     * 获取配置信息
     */
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
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        if (ids == null || ids.length == 0) {
            return R.error("删除 ID 不能为空");
        }
        boolean removed = configService.removeByIds(Arrays.asList(ids));
        return removed ? R.ok() : R.error("删除失败");
    }
}
