package com.controller;

import java.util.Arrays;
import java.util.Map;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.DoctorEntity;
import com.entity.UserEntity;
import com.service.DoctorService;
import com.service.UserService;
import com.utils.PageUtils;
import com.utils.R;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.servlet.http.HttpServletRequest;

/**
 * 医生信息管理接口
 */
@RestController
@RequestMapping("/api/v1/doctors")
@Tag(name = "医生管理", description = "医生信息管理相关接口")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;
    
    @Autowired
    private UserService userService;

    /**
     * 分页查询医生信息
     */
    @Operation(summary = "分页查询医生信息", description = "按条件分页查询医生信息")
    @Parameters({
            @Parameter(name = "page", description = "页码", required = true),
            @Parameter(name = "limit", description = "每页数量", required = true)
    })
    @GetMapping
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = doctorService.queryPage(params);
        return R.ok().put("data", page);
    }

    /**
     * 获取医生详情
     */
    @Operation(summary = "获取医生详情", description = "根据ID获取医生详细信息")
    @Parameter(name = "id", description = "医生ID", required = true)
    @GetMapping("/{id}")
    public R info(@PathVariable("id") Integer id) {
        DoctorEntity doctor = doctorService.getById(id);
        if (doctor == null) {
            return R.error("医生信息不存在");
        }
        
        // 获取用户基本信息
        UserEntity user = userService.getById(doctor.getUserId());
        if (user != null) {
            return R.ok().put("data", doctor).put("user", user);
        }
        
        return R.ok().put("data", doctor);
    }

    /**
     * 获取当前登录医生信息
     */
    @Operation(summary = "获取当前登录医生信息", description = "获取当前登录用户的医生信息")
    @GetMapping("/profile")
    public R profile(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return R.error("未登录");
        }
        
        DoctorEntity doctor = doctorService.getByUserId(userId);
        if (doctor == null) {
            return R.error("医生信息不存在");
        }
        
        // 获取用户基本信息
        UserEntity user = userService.getById(userId);
        if (user != null) {
            return R.ok().put("data", doctor).put("user", user);
        }
        
        return R.ok().put("data", doctor);
    }

    /**
     * 保存医生信息
     */
    @Operation(summary = "保存医生信息", description = "创建新的医生信息")
    @PostMapping
    public R save(@RequestBody DoctorEntity doctor) {
        doctorService.save(doctor);
        return R.ok();
    }

    /**
     * 修改医生信息
     */
    @Operation(summary = "修改医生信息", description = "更新已有的医生信息")
    @Parameter(name = "id", description = "医生ID", required = true)
    @PutMapping("/{id}")
    public R update(@PathVariable("id") Integer id, @RequestBody DoctorEntity doctor) {
        doctor.setId(id);
        doctorService.updateById(doctor);
        return R.ok();
    }

    /**
     * 删除医生信息
     */
    @Operation(summary = "删除医生信息", description = "删除指定的医生信息")
    @Parameter(name = "id", description = "医生ID", required = true)
    @DeleteMapping("/{id}")
    public R delete(@PathVariable("id") Integer id) {
        doctorService.removeById(id);
        return R.ok();
    }

    /**
     * 批量删除医生信息
     */
    @Operation(summary = "批量删除医生信息", description = "批量删除多个医生信息")
    @Parameter(name = "ids", description = "医生ID数组", required = true)
    @DeleteMapping("/batch")
    public R deleteBatch(@RequestBody Integer[] ids) {
        doctorService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }
}