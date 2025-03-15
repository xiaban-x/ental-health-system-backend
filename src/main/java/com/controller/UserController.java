
package com.controller;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.UserEntity;
import com.service.TokenService;
import com.service.UserService;
import com.utils.PageUtils;
import com.utils.R;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 登录相关
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "用户管理", description = "用户登录注册及信息管理相关接口")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    /**
     * 用户认证相关接口
     */
    @PostMapping("/auth/login")
    @Operation(summary = "用户登录", description = "用户登录并返回token")
    @IgnoreAuth
    public R login(@RequestParam String username,
            @RequestParam String password,
            @RequestParam(required = false) String captcha) {
        UserEntity user = userService.getOne(new QueryWrapper<UserEntity>().eq("username", username));
        if (user == null || !user.getPassword().equals(password)) {
            return R.error("账号或密码不正确");
        }
        String token = tokenService.generateToken(user.getId(), username, "users", user.getRole());
        return R.ok().put("token", token);
    }

    @PostMapping("/auth/register")
    @Operation(summary = "用户注册", description = "新用户注册")
    @IgnoreAuth
    public R register(@RequestBody UserEntity user) {
        if (userService.getOne(new QueryWrapper<UserEntity>().eq("username", user.getUsername())) != null) {
            return R.error("用户已存在");
        }
        userService.save(user);
        return R.ok().put("data", user);
    }

    @PostMapping("/auth/logout")
    @Operation(summary = "用户退出", description = "注销用户登录状态")
    public R logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return R.ok();
    }

    @PostMapping("/auth/reset-password")
    @Operation(summary = "重置密码", description = "重置用户密码为默认密码")
    @IgnoreAuth
    public R resetPassword(@RequestParam String username) {
        UserEntity user = userService.getOne(new QueryWrapper<UserEntity>().eq("username", username));
        if (user == null) {
            return R.error("账号不存在");
        }
        user.setPassword("123456");
        userService.updateById(user);
        return R.ok().put("message", "密码已重置为：123456");
    }

    /**
     * 用户管理接口
     */
    @GetMapping
    @Operation(summary = "获取用户列表", description = "分页获取用户信息列表")
    public R getUsers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            UserEntity user) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        // 添加查询条件
        if (user != null) {
            // 根据实际需求添加条件
        }
        PageUtils pageResult = userService.queryPage(
                Map.of("page", page, "limit", size),
                queryWrapper);
        return R.ok().put("data", pageResult);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取用户详情", description = "根据ID获取用户详细信息")
    public R getUser(@PathVariable Long id) {
        UserEntity user = userService.getById(id);
        if (user == null) {
            return R.error("用户不存在");
        }
        return R.ok().put("data", user);
    }

    @GetMapping("/current")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    public R getCurrentUser(HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        UserEntity user = userService.getById(userId);
        if (user == null) {
            return R.error("用户不存在");
        }
        return R.ok().put("data", user);
    }

    @PostMapping
    @Operation(summary = "创建用户", description = "添加新用户信息")
    public R createUser(@RequestBody UserEntity user) {
        if (userService.getOne(new QueryWrapper<UserEntity>().eq("username", user.getUsername())) != null) {
            return R.error("用户已存在");
        }
        userService.save(user);
        return R.ok().put("data", user);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新用户", description = "更新已有的用户信息")
    public R updateUser(@PathVariable Long id, @RequestBody UserEntity user) {
        user.setId(id);
        UserEntity existingUser = userService.getOne(
                new QueryWrapper<UserEntity>().eq("username", user.getUsername()));
        if (existingUser != null && !existingUser.getId().equals(id)) {
            return R.error("用户名已存在");
        }
        boolean updated = userService.updateById(user);
        return updated ? R.ok().put("data", user) : R.error("更新失败");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "删除指定用户")
    public R deleteUser(@PathVariable Long id) {
        boolean removed = userService.removeById(id);
        return removed ? R.ok() : R.error("删除失败");
    }

    @DeleteMapping("/batch")
    @Operation(summary = "批量删除用户", description = "批量删除多个用户")
    public R batchDeleteUsers(@RequestBody Long[] ids) {
        if (ids == null || ids.length == 0) {
            return R.error("删除ID不能为空");
        }
        boolean removed = userService.removeBatchByIds(Arrays.asList(ids));
        return removed ? R.ok() : R.error("批量删除失败");
    }
}
