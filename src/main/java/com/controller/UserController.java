package com.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.CounselorEntity;
import com.entity.StudentEntity;
import com.entity.UserEntity;
import com.service.CounselorService;
import com.service.StudentService;
import com.service.TokenService;
import com.service.UserService;
import com.utils.MD5Util;
import com.utils.R;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户相关接口
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "用户管理", description = "用户注册、登录、信息管理相关接口")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CounselorService counselorService;

    /**
     * 用户注册
     */
    @Operation(summary = "用户注册", description = "创建新用户并根据角色创建对应的角色信息")
    @Parameters({
            @Parameter(name = "username", description = "用户名", required = true),
            @Parameter(name = "password", description = "密码", required = true),
            @Parameter(name = "role", description = "角色(student/teacher)", required = true)
    })
    @IgnoreAuth
    @PostMapping("/register")
    @Transactional
    public R register(@RequestBody Map<String, Object> params) {
        String username = params.get("username").toString();
        String password = params.get("password").toString();
        String role = params.get("role").toString();

        // 验证角色是否有效
        if (!role.equals("student") && !role.equals("teacher")) {
            return R.error("无效的角色类型");
        }

        // 检查用户名是否已存在
        UserEntity existUser = userService.getOne(new QueryWrapper<UserEntity>().eq("username", username));
        if (existUser != null) {
            return R.error("用户名已存在");
        }

        // 创建用户
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(password); // 实际应用中应该加密存储
        user.setRole(role);
        userService.save(user);

        // 根据角色创建对应的信息
        if (role.equals("student")) {
            StudentEntity student = new StudentEntity();
            student.setUserId(user.getId());
            // 设置其他学生信息
            studentService.save(student);
        } else if (role.equals("teacher")) {
            CounselorEntity counselor = new CounselorEntity();
            counselor.setUserId(user.getId());

            // 设置教师相关信息
            if (params.containsKey("title")) {
                counselor.setTitle(params.get("title").toString());
            }
            if (params.containsKey("specialty")) {
                counselor.setSpecialty(params.get("specialty").toString());
            }
            if (params.containsKey("introduction")) {
                counselor.setIntroduction(params.get("introduction").toString());
            }
            if (params.containsKey("employeeId")) {
                counselor.setEmployeeId(params.get("employeeId").toString());
            }
            if (params.containsKey("department")) {
                counselor.setDepartment(params.get("department").toString());
            }
            if (params.containsKey("officeLocation")) {
                counselor.setOfficeLocation(params.get("officeLocation").toString());
            }

            // 设置状态为可用
            counselor.setStatus(1);

            // 保存教师信息
            counselorService.save(counselor);
        }

        return R.ok();
    }

    /**
     * 用户登录
     */
    @Operation(summary = "用户登录", description = "用户登录并返回token")
    @Parameters({
            @Parameter(name = "username", description = "用户名", required = true),
            @Parameter(name = "password", description = "密码", required = true)
    })
    @IgnoreAuth
    @PostMapping("/login")
    public R login(@RequestBody Map<String, Object> params) {
        String username = params.get("username").toString();
        String password = params.get("password").toString();

        UserEntity user = userService.getOne(new QueryWrapper<UserEntity>().eq("username", username));
        if (user == null || !user.getPassword().equals(MD5Util.md5(password))) {
            return R.error("用户名或密码错误");
        }

        // 生成token
        String token = tokenService.generateToken(user.getId(), username, "user", user.getRole());

        // 获取角色信息
        Map<String, Object> roleInfo = null;
        if ("student".equals(user.getRole())) {
            StudentEntity student = studentService.getByUserId(user.getId());
            if (student != null) {
                roleInfo = Map.of(
                        "studentId", student.getStudentId(),
                        "major", student.getMajor(),
                        "className", student.getClassName(),
                        "grade", student.getGrade());
            }
        } else if ("teacher".equals(user.getRole())) {
            CounselorEntity counselor = counselorService.getByUserId(user.getId());
            if (counselor != null) {
                // 创建一个可变的Map
                Map<String, Object> teacherInfo = new HashMap<>();

                // 添加教师信息
                teacherInfo.put("id", counselor.getId());
                teacherInfo.put("title", counselor.getTitle());
                teacherInfo.put("specialty", counselor.getSpecialty());
                teacherInfo.put("introduction", counselor.getIntroduction());
                teacherInfo.put("employeeId", counselor.getEmployeeId());
                teacherInfo.put("department", counselor.getDepartment());
                teacherInfo.put("officeLocation", counselor.getOfficeLocation());
                teacherInfo.put("status", counselor.getStatus());

                roleInfo = teacherInfo;
            }
        }

        return R.ok()
                .put("token", token)
                .put("userId", user.getId())
                .put("username", user.getUsername())
                .put("role", user.getRole())
                .put("roleInfo", roleInfo);
    }

    /**
     * 获取用户信息
     */
    @Operation(summary = "获取用户信息", description = "获取当前登录用户的信息")
    @GetMapping("/profile")
    public R info(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        UserEntity user = userService.getById(userId);
        if (user == null) {
            return R.error("用户不存在");
        }

        // 获取角色信息
        Object roleInfo = null;
        if ("student".equals(user.getRole())) {
            roleInfo = studentService.getByUserId(userId);
        } else if ("teacher".equals(user.getRole())) {
            roleInfo = counselorService.getByUserId(userId);
        }

        // 创建包含user和roleInfo的Map作为data字段的值
        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("roleInfo", roleInfo);

        return R.ok().put("data", data);
    }

    /**
     * 修改用户信息
     */
    @Operation(summary = "修改用户信息", description = "修改当前登录用户的基本信息")
    @PutMapping("/profile")
    @Transactional
    public R update(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");

        // 获取当前用户信息
        UserEntity user = userService.getById(userId);
        if (user == null) {
            return R.error("用户不存在");
        }

        // 更新用户基本信息
        if (params.get("name") != null) {
            user.setName(params.get("name").toString());
        }
        if (params.get("sex") != null) {
            user.setSex(params.get("sex").toString());
        }
        if (params.get("phone") != null) {
            user.setPhone(params.get("phone").toString());
        }
        if (params.get("email") != null) {
            user.setEmail(params.get("email").toString());
        }
        if (params.get("avatar") != null) {
            user.setAvatar(params.get("avatar").toString());
        }

        // 更新时间
        user.setUpdatedAt(new Date());

        // 不允许修改用户名和密码
        user.setUsername(null);
        user.setPassword(null);

        // 更新用户基本信息
        userService.updateById(user);

        // 根据角色更新对应的角色信息
        if ("student".equals(user.getRole())) {
            StudentEntity student = studentService.getByUserId(userId);
            if (student != null) {
                if (params.get("studentId") != null) {
                    student.setStudentId(params.get("studentId").toString());
                }
                if (params.get("major") != null) {
                    student.setMajor(params.get("major").toString());
                }
                if (params.get("className") != null) {
                    student.setClassName(params.get("className").toString());
                }
                if (params.get("grade") != null) {
                    student.setGrade(params.get("grade").toString());
                }
                studentService.updateById(student);
            }
        } else if ("teacher".equals(user.getRole())) {
            CounselorEntity counselor = counselorService.getByUserId(userId);
            if (counselor != null) {
                // 更新教师信息
                if (params.get("title") != null) {
                    counselor.setTitle(params.get("title").toString());
                }
                if (params.get("specialty") != null) {
                    counselor.setSpecialty(params.get("specialty").toString());
                }
                if (params.get("introduction") != null) {
                    counselor.setIntroduction(params.get("introduction").toString());
                }
                if (params.get("employeeId") != null) {
                    counselor.setEmployeeId(params.get("employeeId").toString());
                }
                if (params.get("department") != null) {
                    counselor.setDepartment(params.get("department").toString());
                }
                if (params.get("officeLocation") != null) {
                    counselor.setOfficeLocation(params.get("officeLocation").toString());
                }

                counselorService.updateById(counselor);
            }
        }

        // 获取更新后的用户和角色信息
        UserEntity updatedUser = userService.getById(userId);
        Object roleInfo = null;
        if ("student".equals(user.getRole())) {
            roleInfo = studentService.getByUserId(userId);
        } else if ("teacher".equals(user.getRole())) {
            roleInfo = counselorService.getByUserId(userId);
        }

        // 创建包含user和roleInfo的Map作为data字段的值
        Map<String, Object> data = new HashMap<>();
        data.put("user", updatedUser);
        data.put("roleInfo", roleInfo);

        return R.ok().put("data", data);
    }

    /**
     * 修改密码
     */
    @Operation(summary = "修改密码", description = "修改当前登录用户的密码")
    @Parameters({
            @Parameter(name = "oldPassword", description = "旧密码", required = true),
            @Parameter(name = "newPassword", description = "新密码", required = true)
    })
    @PutMapping("/password")
    public R updatePassword(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        String oldPassword = params.get("oldPassword").toString();
        String newPassword = params.get("newPassword").toString();

        UserEntity user = userService.getById(userId);
        if (user == null) {
            return R.error("用户不存在");
        }

        if (!user.getPassword().equals(MD5Util.md5(oldPassword))) {
            return R.error("原密码错误");
        }

        user.setPassword(MD5Util.md5(newPassword));
        userService.updateById(user);

        return R.ok();
    }
}
