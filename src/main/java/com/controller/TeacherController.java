package com.controller;

import java.util.Arrays;
import java.util.Map;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.TeacherEntity;
import com.entity.UserEntity;
import com.service.TeacherService;
import com.service.UserService;
import com.utils.PageUtils;
import com.utils.R;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.servlet.http.HttpServletRequest;

/**
 * 教师信息管理接口
 */
@RestController
@RequestMapping("/api/v1/teachers")
@Tag(name = "教师管理", description = "教师信息管理相关接口")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;
    
    @Autowired
    private UserService userService;

    /**
     * 分页查询教师信息
     */
    @Operation(summary = "分页查询教师信息", description = "按条件分页查询教师信息")
    @Parameters({
            @Parameter(name = "page", description = "页码", required = true),
            @Parameter(name = "limit", description = "每页数量", required = true)
    })
    @GetMapping
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = teacherService.queryPage(params);
        return R.ok().put("data", page);
    }

    /**
     * 获取教师详情
     */
    @Operation(summary = "获取教师详情", description = "根据ID获取教师详细信息")
    @Parameter(name = "id", description = "教师ID", required = true)
    @GetMapping("/{id}")
    public R info(@PathVariable("id") Integer id) {
        TeacherEntity teacher = teacherService.getById(id);
        if (teacher == null) {
            return R.error("教师信息不存在");
        }
        
        // 获取用户基本信息
        UserEntity user = userService.getById(teacher.getUserId());
        if (user != null) {
            return R.ok().put("data", teacher).put("user", user);
        }
        
        return R.ok().put("data", teacher);
    }

    /**
     * 获取当前登录教师信息
     */
    @Operation(summary = "获取当前登录教师信息", description = "获取当前登录用户的教师信息")
    @GetMapping("/profile")
    public R profile(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return R.error("未登录");
        }
        
        TeacherEntity teacher = teacherService.getByUserId(userId);
        if (teacher == null) {
            return R.error("教师信息不存在");
        }
        
        // 获取用户基本信息
        UserEntity user = userService.getById(userId);
        if (user != null) {
            return R.ok().put("data", teacher).put("user", user);
        }
        
        return R.ok().put("data", teacher);
    }

    /**
     * 保存教师信息
     */
    @Operation(summary = "保存教师信息", description = "创建新的教师信息")
    @PostMapping
    public R save(@RequestBody TeacherEntity teacher) {
        teacherService.save(teacher);
        return R.ok();
    }

    /**
     * 修改教师信息
     */
    @Operation(summary = "修改教师信息", description = "更新已有的教师信息")
    @Parameter(name = "id", description = "教师ID", required = true)
    @PutMapping("/{id}")
    public R update(@PathVariable("id") Integer id, @RequestBody TeacherEntity teacher) {
        teacher.setId(id);
        teacherService.updateById(teacher);
        return R.ok();
    }

    /**
     * 删除教师信息
     */
    @Operation(summary = "删除教师信息", description = "删除指定的教师信息")
    @Parameter(name = "id", description = "教师ID", required = true)
    @DeleteMapping("/{id}")
    public R delete(@PathVariable("id") Integer id) {
        teacherService.removeById(id);
        return R.ok();
    }

    /**
     * 批量删除教师信息
     */
    @Operation(summary = "批量删除教师信息", description = "批量删除多个教师信息")
    @Parameter(name = "ids", description = "教师ID数组", required = true)
    @DeleteMapping("/batch")
    public R deleteBatch(@RequestBody Integer[] ids) {
        teacherService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }
}