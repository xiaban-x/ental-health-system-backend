package com.controller;

import java.util.Arrays;
import java.util.Map;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.StudentEntity;
import com.entity.UserEntity;
import com.service.StudentService;
import com.service.UserService;
import com.utils.PageUtils;
import com.utils.R;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.servlet.http.HttpServletRequest;

/**
 * 学生信息管理接口
 */
@RestController
@RequestMapping("/api/v1/students")
@Tag(name = "学生管理", description = "学生信息管理相关接口")
public class StudentController {
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private UserService userService;

    /**
     * 分页查询学生信息
     */
    @Operation(summary = "分页查询学生信息", description = "按条件分页查询学生信息")
    @Parameters({
            @Parameter(name = "page", description = "页码", required = true),
            @Parameter(name = "limit", description = "每页数量", required = true)
    })
    @GetMapping
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = studentService.queryPage(params);
        return R.ok().put("data", page);
    }

    /**
     * 获取学生详情
     */
    @Operation(summary = "获取学生详情", description = "根据ID获取学生详细信息")
    @Parameter(name = "id", description = "学生ID", required = true)
    @GetMapping("/{id}")
    public R info(@PathVariable("id") Integer id) {
        StudentEntity student = studentService.getById(id);
        if (student == null) {
            return R.error("学生信息不存在");
        }
        
        // 获取用户基本信息
        UserEntity user = userService.getById(student.getUserId());
        if (user != null) {
            return R.ok().put("data", student).put("user", user);
        }
        
        return R.ok().put("data", student);
    }

    /**
     * 获取当前登录学生信息
     */
    @Operation(summary = "获取当前登录学生信息", description = "获取当前登录用户的学生信息")
    @GetMapping("/profile")
    public R profile(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return R.error("未登录");
        }
        
        StudentEntity student = studentService.getByUserId(userId);
        if (student == null) {
            return R.error("学生信息不存在");
        }
        
        // 获取用户基本信息
        UserEntity user = userService.getById(userId);
        if (user != null) {
            return R.ok().put("data", student).put("user", user);
        }
        
        return R.ok().put("data", student);
    }

    /**
     * 保存学生信息
     */
    @Operation(summary = "保存学生信息", description = "创建新的学生信息")
    @PostMapping
    public R save(@RequestBody StudentEntity student) {
        studentService.save(student);
        return R.ok();
    }

    /**
     * 修改学生信息
     */
    @Operation(summary = "修改学生信息", description = "更新已有的学生信息")
    @Parameter(name = "id", description = "学生ID", required = true)
    @PutMapping("/{id}")
    public R update(@PathVariable("id") Integer id, @RequestBody StudentEntity student) {
        student.setId(id);
        studentService.updateById(student);
        return R.ok();
    }

    /**
     * 删除学生信息
     */
    @Operation(summary = "删除学生信息", description = "删除指定的学生信息")
    @Parameter(name = "id", description = "学生ID", required = true)
    @DeleteMapping("/{id}")
    public R delete(@PathVariable("id") Integer id) {
        studentService.removeById(id);
        return R.ok();
    }

    /**
     * 批量删除学生信息
     */
    @Operation(summary = "批量删除学生信息", description = "批量删除多个学生信息")
    @Parameter(name = "ids", description = "学生ID数组", required = true)
    @DeleteMapping("/batch")
    public R deleteBatch(@RequestBody Integer[] ids) {
        studentService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }
}