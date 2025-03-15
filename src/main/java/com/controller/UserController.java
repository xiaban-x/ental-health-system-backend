
package com.controller;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.TokenEntity;
import com.entity.UserEntity;
import com.service.TokenService;
import com.service.UserService;
import com.utils.CommonUtil;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.ValidatorUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 登录相关
 */
/**
 * 用户管理接口
 */
@Tag(name = "用户管理", description = "用户登录注册及信息管理相关接口")
@RequestMapping("users")
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private TokenService tokenService;

    /**
     * 登录
     */
    @Operation(summary = "用户登录", description = "用户登录并返回token")
    @Parameters({
        @Parameter(name = "username", description = "用户名", required = true),
        @Parameter(name = "password", description = "密码", required = true),
        @Parameter(name = "captcha", description = "验证码"),
        @Parameter(name = "request", description = "HTTP请求对象")
    })
    @IgnoreAuth
    @PostMapping(value = "/login")
    public R login(String username, String password, String captcha, HttpServletRequest request) {
        UserEntity user = userService.getOne(new QueryWrapper<UserEntity>().eq("username", username));
        if (user == null || !user.getPassword().equals(password)) {
            return R.error("账号或密码不正确");
        }
        String token = tokenService.generateToken(user.getId(), username, "users", user.getRole());
        return R.ok().put("token", token);
    }

    /**
     * 注册
     */
    @Operation(summary = "用户注册", description = "新用户注册")
    @Parameter(name = "user", description = "用户信息", required = true)
    @IgnoreAuth
    @PostMapping(value = "/register")
    public R register(@RequestBody UserEntity user) {
        // ValidatorUtils.validateEntity(user);
        if (userService.getOne(new QueryWrapper<UserEntity>().eq("username", user.getUsername())) != null) {
            return R.error("用户已存在");
        }
        userService.save(user);
        return R.ok();
    }

    /**
     * 退出
     */
    @Operation(summary = "用户退出", description = "注销用户登录状态")
    @Parameter(name = "request", description = "HTTP请求对象")
    @GetMapping(value = "logout")
    public R logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return R.ok("退出成功");
    }

    /**
     * 密码重置
     */
    @Operation(summary = "重置密码", description = "重置用户密码为默认密码")
    @Parameters({
        @Parameter(name = "username", description = "用户名", required = true),
        @Parameter(name = "request", description = "HTTP请求对象")
    })
    @IgnoreAuth
    @RequestMapping(value = "/resetPass")
    public R resetPass(String username, HttpServletRequest request) {
        UserEntity user = userService.getOne(new QueryWrapper<UserEntity>().eq("username", username));
        if (user == null) {
            return R.error("账号不存在");
        }
        user.setPassword("123456");
        userService.update(user, null);
        return R.ok("密码已重置为：123456");
    }

    /**
     * 列表
     */
    @Operation(summary = "分页查询用户", description = "获取用户信息的分页列表")
    @Parameters({
        @Parameter(name = "params", description = "分页参数", required = true),
        @Parameter(name = "user", description = "用户查询条件")
    })
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, UserEntity user) {
        QueryWrapper<UserEntity> ew = new QueryWrapper<UserEntity>();
        PageUtils page = userService.queryPage(params, ew);
        MPUtil.sort(MPUtil.between(MPUtil.allLike(ew, user), params), params);
        return R.ok().put("data", page);
    }

    /**
     * 列表
     */
    @Operation(summary = "获取用户列表", description = "获取所有用户信息列表")
    @Parameter(name = "user", description = "用户查询条件")
    @RequestMapping("/list")
    public R list(UserEntity user) {
        QueryWrapper<UserEntity> ew = new QueryWrapper<UserEntity>();
        ew.allEq(MPUtil.allEQMapPre(user, "user"));
        return R.ok().put("data", userService.selectListView(ew));
    }

    /**
     * 信息
     */
    @Operation(summary = "获取用户信息", description = "根据ID获取用户详细信息")
    @Parameter(name = "id", description = "用户ID", required = true)
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") String id) {
        UserEntity user = userService.getById(id);
        return R.ok().put("data", user);
    }

    /**
     * 获取用户的session用户信息
     */
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    @Parameter(name = "request", description = "HTTP请求对象")
    @RequestMapping("/session")
    public R getCurrUser(HttpServletRequest request) {
        Long id = (Long) request.getSession().getAttribute("userId");
        UserEntity user = userService.getById(id);
        return R.ok().put("data", user);
    }

    /**
     * 保存
     */
    @Operation(summary = "新增用户", description = "添加新用户信息")
    @Parameter(name = "user", description = "用户信息", required = true)
    @PostMapping("/save")
    public R save(@RequestBody UserEntity user) {
        // ValidatorUtils.validateEntity(user);
        if (userService.getOne(new QueryWrapper<UserEntity>().eq("username", user.getUsername())) != null) {
            return R.error("用户已存在");
        }
        userService.save(user);
        return R.ok();
    }

    /**
     * 修改
     */
    @Operation(summary = "更新用户信息", description = "更新已有的用户信息")
    @Parameter(name = "user", description = "用户信息", required = true)
    @RequestMapping("/update")
    public R update(@RequestBody UserEntity user) {
        // ValidatorUtils.validateEntity(user);
        UserEntity u = userService.getOne(new QueryWrapper<UserEntity>().eq("username", user.getUsername()));
        if (u != null && u.getId() != user.getId() && u.getUsername().equals(user.getUsername())) {
            return R.error("用户名已存在。");
        }
        userService.updateById(user);// 全部更新
        return R.ok();
    }

    /**
     * 删除
     */
    @Operation(summary = "删除用户", description = "批量删除用户信息")
    @Parameter(name = "ids", description = "用户ID数组", required = true)
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        userService.removeBatchByIds(Arrays.asList(ids));
        return R.ok();
    }
}
