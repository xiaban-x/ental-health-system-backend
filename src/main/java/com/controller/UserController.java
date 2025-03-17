package com.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.annotation.IgnoreAuth;
import com.entity.TokenEntity;
import com.entity.UserEntity;

import com.service.UserService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;

/**
 * 用户
 * 后端接口
 * 
 * @author
 * @email
 * @date 2021-05-04 17:24:35
 */
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 用户管理接口
 */
@Tag(name = "用户管理", description = "用户的登录注册及信息管理相关接口")
@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "用户管理", description = "用户的登录注册及信息管理相关接口")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    /**
     * 认证相关接口
     */
    @PostMapping("/auth/login")
    @Operation(summary = "用户登录", description = "普通用户登录并返回token")
    @IgnoreAuth
    public R login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");
        String captcha = loginData.get("captcha");

        if (username == null || password == null) {
            return R.error("账号和密码不能为空");
        }

        UserEntity user = userService.getOne(new QueryWrapper<UserEntity>().eq("username", username));
        if (user == null || !user.getPassword().equals(password)) {
            return R.error("账号或密码不正确");
        }
        String token = tokenService.generateToken(user.getId(), username, "user", "用户");
        return R.ok().put("token", token);
    }

    @PostMapping("/auth/register")
    @Operation(summary = "用户注册", description = "新用户注册")
    @IgnoreAuth
    public R register(@RequestBody UserEntity user) {
        System.out.println("user ==>" + user + user.getUsername());
        if (userService.getOne(new QueryWrapper<UserEntity>().eq("username", user.getUsername())) != null) {
            return R.error("用户已存在");
        }
        user.setId((int) new Date().getTime());
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
    @Operation(summary = "获取用户列表", description = "分页获取用户列表")
    public R getUsers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            UserEntity user) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("limit", size);

        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        PageUtils pageResult = userService.queryPage(params, queryWrapper);
        return R.ok().put("data", pageResult);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取用户详情", description = "根据ID获取用户详细信息")
    public R getUser(@PathVariable Integer id) {
        System.out.println("id ==>" + id);
        UserEntity user = userService.getById(id);
        if (user == null) {
            return R.error("用户不存在");
        }
        return R.ok().put("data", user);
    }

    @GetMapping("/current")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户信息")
    public R getCurrentUser(HttpServletRequest request) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        UserEntity user = userService.getById(userId);
        if (user == null) {
            return R.error("用户不存在");
        }
        return R.ok().put("data", user);
    }

    @GetMapping("/info")
    @Operation(summary = "获取用户信息", description = "根据token获取当前登录用户信息")
    public R getUserInfo(@RequestHeader("Authorization") String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return R.error("无效的认证头");
        }

        String token = authorization.substring(7); // 去掉 "Bearer " 前缀
        TokenEntity tokenEntity = tokenService.getTokenEntity(token);
        if (tokenEntity == null) {
            return R.error("token已过期或不存在");
        }

        // 获取用户信息
        UserEntity user = userService.getById(tokenEntity.getUserid());
        if (user == null) {
            return R.error("用户不存在");
        }

        return R.ok().put("data", user);
    }

    @PostMapping
    @Operation(summary = "创建用户", description = "添加新用户")
    public R createUser(@RequestBody UserEntity user) {
        if (userService.getOne(new QueryWrapper<UserEntity>().eq("username", user.getUsername())) != null) {
            return R.error("用户已存在");
        }
        user.setId((int) new Date().getTime());
        userService.save(user);
        return R.ok().put("data", user);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新用户", description = "更新用户信息")
    public R updateUser(@PathVariable Integer id, @RequestBody UserEntity user) {
        user.setId(id);
        if (!userService.updateById(user)) {
            return R.error("更新失败");
        }
        return R.ok().put("data", user);
    }

    @PutMapping("/profile")
    @Operation(summary = "更新个人信息", description = "根据token更新当前登录用户的个人信息")
    public R updateProfile(@RequestHeader("Authorization") String authorization, @RequestBody UserEntity updateInfo) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return R.error("无效的认证头");
        }

        String token = authorization.substring(7);
        TokenEntity tokenEntity = tokenService.getTokenEntity(token);
        if (tokenEntity == null) {
            return R.error("token已过期或不存在");
        }

        // 获取当前用户
        UserEntity currentUser = userService.getById(tokenEntity.getUserid());
        if (currentUser == null) {
            return R.error("用户不存在");
        }

        // 只更新允许的字段
        currentUser.setName(updateInfo.getName());
        currentUser.setStudentId(updateInfo.getStudentId());
        currentUser.setSex(updateInfo.getSex());
        currentUser.setPhone(updateInfo.getPhone());
        currentUser.setEmail(updateInfo.getEmail());
        currentUser.setAvatar(updateInfo.getAvatar());

        // 更新用户信息
        if (!userService.updateById(currentUser)) {
            return R.error("更新失败");
        }

        return R.ok().put("data", currentUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "删除指定用户")
    public R deleteUser(@PathVariable Integer id) {
        if (!userService.removeById(id)) {
            return R.error("删除失败");
        }
        return R.ok();
    }

    @DeleteMapping("/batch")
    @Operation(summary = "批量删除用户", description = "批量删除多个用户")
    public R batchDeleteUsers(@RequestBody Integer[] ids) {
        if (ids == null || ids.length == 0) {
            return R.error("删除ID不能为空");
        }
        if (!userService.removeBatchByIds(Arrays.asList(ids))) {
            return R.error("批量删除失败");
        }
        return R.ok();
    }

    /**
     * 统计接口
     */
    @GetMapping("/statistics/{columnName}/{type}")
    @Operation(summary = "获取统计数据", description = "获取指定时间范围内的用户统计数据")
    public R getStatistics(
            @PathVariable String columnName,
            @PathVariable String type,
            @RequestParam(required = false) Integer remindstart,
            @RequestParam(required = false) Integer remindend) {

        Map<String, Object> params = new HashMap<>();
        params.put("column", columnName);
        params.put("type", type);

        if (type.equals("2")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            if (remindstart != null) {
                c.setTime(new Date());
                c.add(Calendar.DAY_OF_MONTH, remindstart);
                params.put("remindstart", sdf.format(c.getTime()));
            }
            if (remindend != null) {
                c.setTime(new Date());
                c.add(Calendar.DAY_OF_MONTH, remindend);
                params.put("remindend", sdf.format(c.getTime()));
            }
        }

        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        if (params.get("remindstart") != null) {
            queryWrapper.ge(columnName, params.get("remindstart"));
        }
        if (params.get("remindend") != null) {
            queryWrapper.le(columnName, params.get("remindend"));
        }

        int count = (int) userService.count(queryWrapper);
        return R.ok().put("count", count);
    }
}
