package com.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.annotation.IgnoreAuth;

import com.entity.YonghuEntity;
import com.entity.view.YonghuView;

import com.service.YonghuService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MD5Util;
import com.utils.MPUtil;
import com.utils.CommonUtil;

/**
 * 用户
 * 后端接口
 * 
 * @author
 * @email
 * @date 2021-05-04 17:24:35
 */
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 用户管理接口
 */
@Tag(name = "普通用户管理", description = "普通用户的登录注册及信息管理相关接口")
@RestController
@RequestMapping("/yonghu")
public class YonghuController {
    @Autowired
    private YonghuService yonghuService;

    @Autowired
    private TokenService tokenService;

    /**
     * 登录
     */
    @Operation(summary = "用户登录", description = "普通用户登录并返回token")
    @Parameters({
        @Parameter(name = "username", description = "用户账号", required = true),
        @Parameter(name = "password", description = "密码", required = true),
        @Parameter(name = "captcha", description = "验证码"),
        @Parameter(name = "request", description = "HTTP请求对象")
    })
    @IgnoreAuth
    @RequestMapping(value = "/login")
    public R login(String username, String password, String captcha, HttpServletRequest request) {
        YonghuEntity user = yonghuService.getOne(new QueryWrapper<YonghuEntity>().eq("zhanghao", username));
        if (user == null || !user.getMima().equals(password)) {
            return R.error("账号或密码不正确");
        }

        String token = tokenService.generateToken(user.getId(), username, "yonghu", "用户");
        return R.ok().put("token", token);
    }

    /**
     * 注册
     */
    @Operation(summary = "用户注册", description = "新用户注册")
    @Parameter(name = "yonghu", description = "用户信息", required = true)
    @IgnoreAuth
    @RequestMapping("/register")
    public R register(@RequestBody YonghuEntity yonghu) {
        // ValidatorUtils.validateEntity(yonghu);
        YonghuEntity user = yonghuService
                .getOne(new QueryWrapper<YonghuEntity>().eq("zhanghao", yonghu.getZhanghao()));
        if (user != null) {
            return R.error("注册用户已存在");
        }
        Long uId = new Date().getTime();
        yonghu.setId(uId);
        yonghuService.save(yonghu);
        return R.ok();
    }

    /**
     * 退出
     */
    @Operation(summary = "用户退出", description = "注销用户登录状态")
    @Parameter(name = "request", description = "HTTP请求对象")
    @RequestMapping("/logout")
    public R logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return R.ok("退出成功");
    }

    /**
     * 获取用户的session用户信息
     */
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的会话信息")
    @Parameter(name = "request", description = "HTTP请求对象")
    @RequestMapping("/session")
    public R getCurrUser(HttpServletRequest request) {
        Long id = (Long) request.getSession().getAttribute("userId");
        YonghuEntity user = yonghuService.getById(id);
        return R.ok().put("data", user);
    }

    /**
     * 密码重置
     */
    @Operation(summary = "重置密码", description = "重置用户密码为默认密码123456")
    @Parameters({
        @Parameter(name = "username", description = "用户账号", required = true),
        @Parameter(name = "request", description = "HTTP请求对象")
    })
    @IgnoreAuth
    @RequestMapping(value = "/resetPass")
    public R resetPass(String username, HttpServletRequest request) {
        YonghuEntity user = yonghuService.getOne(new QueryWrapper<YonghuEntity>().eq("zhanghao", username));
        if (user == null) {
            return R.error("账号不存在");
        }
        user.setMima("123456");
        yonghuService.updateById(user);
        return R.ok("密码已重置为：123456");
    }

    /**
     * 后端列表
     */
    @Operation(summary = "后台分页查询", description = "管理员获取用户的分页列表")
    @Parameters({
        @Parameter(name = "params", description = "分页参数", required = true),
        @Parameter(name = "yonghu", description = "用户查询条件"),
        @Parameter(name = "request", description = "HTTP请求对象")
    })
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, YonghuEntity yonghu,
            HttpServletRequest request) {
        QueryWrapper<YonghuEntity> ew = new QueryWrapper<YonghuEntity>();
        PageUtils page = yonghuService.queryPage(params,
                (QueryWrapper<YonghuEntity>) MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, yonghu), params), params));

        return R.ok().put("data", page);
    }

    /**
     * 前端列表
     */
    @Operation(summary = "前台分页查询", description = "前端获取用户的分页列表")
    @Parameters({
        @Parameter(name = "params", description = "分页参数", required = true),
        @Parameter(name = "yonghu", description = "用户查询条件"),
        @Parameter(name = "request", description = "HTTP请求对象")
    })
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, YonghuEntity yonghu,
            HttpServletRequest request) {
        QueryWrapper<YonghuEntity> ew = new QueryWrapper<YonghuEntity>();
        PageUtils page = yonghuService.queryPage(params,
                (QueryWrapper<YonghuEntity>) MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, yonghu), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 列表
     */
    @Operation(summary = "获取用户列表", description = "获取所有用户信息列表")
    @Parameter(name = "yonghu", description = "用户查询条件")
    @RequestMapping("/lists")
    public R list(YonghuEntity yonghu) {
        QueryWrapper<YonghuEntity> ew = new QueryWrapper<YonghuEntity>();
        ew.allEq(MPUtil.allEQMapPre(yonghu, "yonghu"));
        return R.ok().put("data", yonghuService.selectListView(ew));
    }

    /**
     * 查询
     */
    @Operation(summary = "查询单个用户", description = "根据条件查询单个用户详情")
    @Parameter(name = "yonghu", description = "用户查询条件", required = true)
    @RequestMapping("/query")
    public R query(YonghuEntity yonghu) {
        QueryWrapper<YonghuEntity> ew = new QueryWrapper<YonghuEntity>();
        ew.allEq(MPUtil.allEQMapPre(yonghu, "yonghu"));
        YonghuView yonghuView = yonghuService.selectView(ew);
        return R.ok("查询用户成功").put("data", yonghuView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        YonghuEntity yonghu = yonghuService.getById(id);
        return R.ok().put("data", yonghu);
    }

    /**
     * 前端详情
     */
    @Operation(summary = "前台获取用户详情", description = "前端根据ID获取用户详情")
    @Parameter(name = "id", description = "用户ID", required = true)
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id) {
        YonghuEntity yonghu = yonghuService.getById(id);
        return R.ok().put("data", yonghu);
    }

    /**
     * 后端保存
     */
    @Operation(summary = "后台保存用户", description = "管理员添加新用户")
    @Parameters({
        @Parameter(name = "yonghu", description = "用户信息", required = true),
        @Parameter(name = "request", description = "HTTP请求对象")
    })
    @RequestMapping("/save")
    public R save(@RequestBody YonghuEntity yonghu, HttpServletRequest request) {
        yonghu.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        // ValidatorUtils.validateEntity(yonghu);
        YonghuEntity user = yonghuService
                .getOne(new QueryWrapper<YonghuEntity>().eq("zhanghao", yonghu.getZhanghao()));
        if (user != null) {
            return R.error("用户已存在");
        }
        yonghu.setId(new Date().getTime());
        yonghuService.save(yonghu);
        return R.ok();
    }

    /**
     * 前端保存
     */
    @Operation(summary = "前台保存用户", description = "前端添加新用户")
    @Parameters({
        @Parameter(name = "yonghu", description = "用户信息", required = true),
        @Parameter(name = "request", description = "HTTP请求对象")
    })
    @RequestMapping("/add")
    public R add(@RequestBody YonghuEntity yonghu, HttpServletRequest request) {
        yonghu.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        // ValidatorUtils.validateEntity(yonghu);
        YonghuEntity user = yonghuService
                .getOne(new QueryWrapper<YonghuEntity>().eq("zhanghao", yonghu.getZhanghao()));
        if (user != null) {
            return R.error("用户已存在");
        }
        yonghu.setId(new Date().getTime());
        yonghuService.save(yonghu);
        return R.ok();
    }

    /**
     * 修改
     */
    @Operation(summary = "更新用户信息", description = "更新已有的用户信息")
    @Parameters({
        @Parameter(name = "yonghu", description = "用户信息", required = true),
        @Parameter(name = "request", description = "HTTP请求对象")
    })
    @RequestMapping("/update")
    public R update(@RequestBody YonghuEntity yonghu, HttpServletRequest request) {
        // ValidatorUtils.validateEntity(yonghu);
        yonghuService.updateById(yonghu);// 全部更新
        return R.ok();
    }

    /**
     * 删除
     */
    @Operation(summary = "删除用户", description = "批量删除用户信息")
    @Parameter(name = "ids", description = "用户ID数组", required = true)
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        yonghuService.removeBatchByIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 提醒接口
     */
    @Operation(summary = "获取提醒记录数", description = "获取指定时间范围内的用户记录数量")
    @Parameters({
        @Parameter(name = "columnName", description = "列名", required = true),
        @Parameter(name = "type", description = "类型(1:数字 2:日期)", required = true),
        @Parameter(name = "request", description = "HTTP请求对象"),
        @Parameter(name = "map", description = "包含remindstart和remindend的参数")
    })
    @RequestMapping("/remind/{columnName}/{type}")
    public R remindCount(@PathVariable("columnName") String columnName, HttpServletRequest request,
            @PathVariable("type") String type, @RequestParam Map<String, Object> map) {
        map.put("column", columnName);
        map.put("type", type);

        if (type.equals("2")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            Date remindStartDate = null;
            Date remindEndDate = null;
            if (map.get("remindstart") != null) {
                Integer remindStart = Integer.parseInt(map.get("remindstart").toString());
                c.setTime(new Date());
                c.add(Calendar.DAY_OF_MONTH, remindStart);
                remindStartDate = c.getTime();
                map.put("remindstart", sdf.format(remindStartDate));
            }
            if (map.get("remindend") != null) {
                Integer remindEnd = Integer.parseInt(map.get("remindend").toString());
                c.setTime(new Date());
                c.add(Calendar.DAY_OF_MONTH, remindEnd);
                remindEndDate = c.getTime();
                map.put("remindend", sdf.format(remindEndDate));
            }
        }

        Wrapper<YonghuEntity> wrapper = new QueryWrapper<YonghuEntity>();
        if (map.get("remindstart") != null) {
            ((QueryWrapper<YonghuEntity>) wrapper).ge(columnName, map.get("remindstart"));
        }
        if (map.get("remindend") != null) {
            ((QueryWrapper<YonghuEntity>) wrapper).le(columnName, map.get("remindend"));
        }

        int count = (int) yonghuService.count(wrapper);
        return R.ok().put("count", count);
    }

}
