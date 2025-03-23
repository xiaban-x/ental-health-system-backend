package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.CounselorEntity;
import com.entity.TimeSlotEntity;
import com.entity.TokenEntity;
import com.entity.UserEntity;
import com.service.CounselorService;
import com.service.TimeSlotService;
import com.service.TokenService;
import com.service.UserService;
import com.utils.PageUtils;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.servlet.http.HttpServletRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 咨询师/教师相关接口
 */
@RestController
@RequestMapping("/api/v1/counselor")
@Tag(name = "咨询师/教师管理", description = "管理咨询师/教师信息的相关接口")
public class CounselorController {

    @Autowired
    private CounselorService counselorService;

    @Autowired
    private TimeSlotService timeSlotService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    /**
     * 获取咨询师列表
     */
    @Operation(summary = "获取咨询师列表", description = "获取所有可用的咨询师列表")
    @GetMapping("/list")
    public R list() {
        QueryWrapper<CounselorEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1); // 只获取状态为可用的咨询师
        queryWrapper.in("role_type", Arrays.asList(1, 3)); // 获取咨询师或兼职角色
        queryWrapper.orderByAsc("id");
        List<CounselorEntity> list = counselorService.list(queryWrapper);
        return R.ok().put("data", list);
    }

    /**
     * 获取教师列表
     */
    @Operation(summary = "获取教师列表", description = "获取所有可用的教师列表")
    @GetMapping("/teachers")
    public R listTeachers() {
        QueryWrapper<CounselorEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1); // 只获取状态为可用的用户
        queryWrapper.in("role_type", Arrays.asList(2, 3)); // 获取教师或兼职角色
        queryWrapper.orderByAsc("id");
        List<CounselorEntity> list = counselorService.list(queryWrapper);
        return R.ok().put("data", list);
    }

    /**
     * 分页查询教师/咨询师信息
     */
    @Operation(summary = "分页查询教师/咨询师信息", description = "按条件分页查询教师/咨询师信息")
    @Parameters({
            @Parameter(name = "page", description = "页码", required = true),
            @Parameter(name = "limit", description = "每页数量", required = true),
            @Parameter(name = "roleType", description = "角色类型：1-咨询师 2-教师 3-兼职")
    })
    @GetMapping("/page")
    public R page(@RequestParam Map<String, Object> params) {
        PageUtils page = counselorService.queryPage(params);
        return R.ok().put("data", page);
    }

    /**
     * 获取咨询师/教师详情
     */
    @Operation(summary = "获取咨询师/教师详情", description = "根据ID获取咨询师/教师的详细信息")
    @Parameter(name = "id", description = "咨询师/教师ID", required = true)
    @GetMapping("/{id}")
    public R info(@PathVariable("id") Integer id) {
        CounselorEntity counselor = counselorService.getById(id);
        if (counselor == null) {
            return R.error("咨询师/教师不存在");
        }

        // 获取用户基本信息
        UserEntity user = userService.getById(counselor.getUserId());
        if (user != null) {
            return R.ok().put("data", counselor).put("user", user);
        }

        return R.ok().put("data", counselor);
    }

    /**
     * 获取当前登录用户的咨询师/教师信息
     */
    @Operation(summary = "获取当前登录用户信息", description = "获取当前登录用户的咨询师/教师信息")
    @GetMapping("/profile")
    public R profile(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return R.error("未登录");
        }

        CounselorEntity counselor = counselorService.getByUserId(userId);
        if (counselor == null) {
            return R.error("咨询师/教师信息不存在");
        }

        // 获取用户基本信息
        UserEntity user = userService.getById(userId);
        if (user != null) {
            return R.ok().put("data", counselor).put("user", user);
        }

        return R.ok().put("data", counselor);
    }

    /**
     * 保存咨询师/教师信息
     */
    @Operation(summary = "保存咨询师/教师信息", description = "创建新的咨询师/教师信息")
    @PostMapping
    public R save(@RequestBody CounselorEntity counselor) {
        counselorService.save(counselor);
        return R.ok();
    }

    /**
     * 修改咨询师/教师信息
     */
    @Operation(summary = "修改咨询师/教师信息", description = "更新已有的咨询师/教师信息")
    @Parameter(name = "id", description = "咨询师/教师ID", required = true)
    @PutMapping("/{id}")
    public R update(@PathVariable("id") Integer id, @RequestBody CounselorEntity counselor) {
        counselor.setId(id);
        counselorService.updateById(counselor);
        return R.ok();
    }

    /**
     * 删除咨询师/教师信息
     */
    @Operation(summary = "删除咨询师/教师信息", description = "删除指定的咨询师/教师信息")
    @Parameter(name = "id", description = "咨询师/教师ID", required = true)
    @DeleteMapping("/{id}")
    public R delete(@PathVariable("id") Integer id) {
        counselorService.removeById(id);
        return R.ok();
    }

    /**
     * 批量删除咨询师/教师信息
     */
    @Operation(summary = "批量删除咨询师/教师信息", description = "批量删除多个咨询师/教师信息")
    @Parameter(name = "ids", description = "咨询师/教师ID数组", required = true)
    @DeleteMapping("/batch")
    public R deleteBatch(@RequestBody Integer[] ids) {
        counselorService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 获取咨询师可用时间段
     */
    @Operation(summary = "获取咨询师可用时间段", description = "获取指定咨询师的所有可用预约时间段")
    @Parameter(name = "id", description = "咨询师ID", required = true)
    @GetMapping("/{id}/time-slots")
    public R timeSlots(@PathVariable("id") Integer id) {
        QueryWrapper<TimeSlotEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("counselor_id", id);
        queryWrapper.ge("start_time", new Date()); // 只获取当前时间之后的时间段
        queryWrapper.orderByAsc("start_time");
        List<TimeSlotEntity> list = timeSlotService.list(queryWrapper);
        return R.ok().put("data", list);
    }

    /**
     * 获取当前登录咨询师的所有时间段
     */
    @Operation(summary = "获取我的时间段", description = "获取当前登录咨询师的所有时间段")
    @GetMapping("/my-time-slots")
    public R myTimeSlots(@RequestHeader("Authorization") String authorization) {
        // 从Authorization头获取token
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return R.error("无效的认证头");
        }
        String token = authorization.substring(7);

        // 获取token中的用户信息
        TokenEntity tokenEntity = tokenService.getTokenEntity(token);
        if (tokenEntity == null) {
            return R.error("token已过期或不存在");
        }

        // 获取咨询师ID
        Integer userId = tokenEntity.getUserId();

        // 查询该咨询师的信息
        QueryWrapper<CounselorEntity> counselorQuery = new QueryWrapper<>();
        counselorQuery.eq("user_id", userId);
        CounselorEntity counselor = counselorService.getOne(counselorQuery);

        if (counselor == null) {
            return R.error("您不是咨询师");
        }

        // 查询该咨询师的所有时间段
        QueryWrapper<TimeSlotEntity> timeSlotQuery = new QueryWrapper<>();
        timeSlotQuery.eq("counselor_id", counselor.getId());
        timeSlotQuery.orderByAsc("start_time");
        List<TimeSlotEntity> timeSlots = timeSlotService.list(timeSlotQuery);

        return R.ok().put("data", timeSlots);
    }

    /**
     * 更新时间段
     */
    @Operation(summary = "更新时间段", description = "咨询师更新时间段信息")
    @Parameters({
            @Parameter(name = "id", description = "时间段ID", required = true),
            @Parameter(name = "timeSlot", description = "时间段信息", required = true)
    })
    @PutMapping("/time-slot/{id}")
    public R updateTimeSlot(@PathVariable("id") Integer id, @RequestBody TimeSlotEntity timeSlot,
            @RequestHeader("Authorization") String authorization) {
        // 从Authorization头获取token
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return R.error("无效的认证头");
        }
        String token = authorization.substring(7);

        // 获取token中的用户信息
        TokenEntity tokenEntity = tokenService.getTokenEntity(token);
        if (tokenEntity == null) {
            return R.error("token已过期或不存在");
        }

        // 获取咨询师ID
        Integer userId = tokenEntity.getUserId();

        // 查询该咨询师的信息
        QueryWrapper<CounselorEntity> counselorQuery = new QueryWrapper<>();
        counselorQuery.eq("user_id", userId);
        CounselorEntity counselor = counselorService.getOne(counselorQuery);

        if (counselor == null) {
            return R.error("您不是咨询师");
        }

        // 查询时间段是否存在
        TimeSlotEntity existingTimeSlot = timeSlotService.getById(id);
        if (existingTimeSlot == null) {
            return R.error("时间段不存在");
        }

        // 检查是否是该咨询师的时间段
        if (!existingTimeSlot.getCounselorId().equals(counselor.getId())) {
            return R.error("您无权修改此时间段");
        }

        // 设置ID
        timeSlot.setId(id);

        // 更新时间段
        boolean updated = timeSlotService.updateById(timeSlot);

        return updated ? R.ok().put("data", timeSlot) : R.error("更新失败");
    }

    /**
     * 删除时间段
     */
    @Operation(summary = "删除时间段", description = "咨询师删除时间段")
    @Parameter(name = "id", description = "时间段ID", required = true)
    @DeleteMapping("/time-slot/{id}")
    public R deleteTimeSlot(@PathVariable("id") Integer id, @RequestHeader("Authorization") String authorization) {
        // 从Authorization头获取token
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return R.error("无效的认证头");
        }
        String token = authorization.substring(7);

        // 获取token中的用户信息
        TokenEntity tokenEntity = tokenService.getTokenEntity(token);
        if (tokenEntity == null) {
            return R.error("token已过期或不存在");
        }

        // 获取咨询师ID
        Integer userId = tokenEntity.getUserId();

        // 查询该咨询师的信息
        QueryWrapper<CounselorEntity> counselorQuery = new QueryWrapper<>();
        counselorQuery.eq("user_id", userId);
        CounselorEntity counselor = counselorService.getOne(counselorQuery);

        if (counselor == null) {
            return R.error("您不是咨询师");
        }

        // 查询时间段是否存在
        TimeSlotEntity existingTimeSlot = timeSlotService.getById(id);
        if (existingTimeSlot == null) {
            return R.error("时间段不存在");
        }

        // 检查是否是该咨询师的时间段
        if (!existingTimeSlot.getCounselorId().equals(counselor.getId())) {
            return R.error("您无权删除此时间段");
        }

        // 删除时间段
        boolean removed = timeSlotService.removeById(id);

        return removed ? R.ok() : R.error("删除失败");
    }

    /**
     * 创建时间段
     */
    @Operation(summary = "创建时间段", description = "咨询师创建可预约的时间段")
    @Parameter(name = "params", description = "时间段信息", required = true)
    @PostMapping("/time-slot")
    public R createTimeSlot(@RequestBody Map<String, String> params,
            @RequestHeader("Authorization") String authorization) {
        // 从Authorization头获取token
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return R.error("无效的认证头");
        }
        String token = authorization.substring(7);

        // 获取token中的用户信息
        TokenEntity tokenEntity = tokenService.getTokenEntity(token);
        if (tokenEntity == null) {
            return R.error("token已过期或不存在");
        }

        // 获取咨询师ID
        Integer userId = tokenEntity.getUserId();

        // 查询该咨询师的信息
        QueryWrapper<CounselorEntity> counselorQuery = new QueryWrapper<>();
        counselorQuery.eq("user_id", userId);
        CounselorEntity counselor = counselorService.getOne(counselorQuery);

        if (counselor == null) {
            return R.error("您不是咨询师");
        }

        // 创建时间段实体
        TimeSlotEntity timeSlot = new TimeSlotEntity();

        // 显式设置ID为null，强制使用数据库的自增策略
        timeSlot.setId(null);

        // 设置咨询师ID
        timeSlot.setCounselorId(counselor.getId());

        // 设置状态为可预约
        timeSlot.setStatus("available");

        try {
            // 获取并转换开始时间和结束时间
            String startTimeStr = params.get("startTime");
            String endTimeStr = params.get("endTime");

            if (startTimeStr == null || endTimeStr == null) {
                return R.error("开始时间和结束时间不能为空");
            }

            // 定义日期格式
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

            // 转换时间字符串为Date对象
            Date startTime = formatter.parse(startTimeStr);
            Date endTime = formatter.parse(endTimeStr);

            // 设置时间段的开始和结束时间
            timeSlot.setStartTime(startTime);
            timeSlot.setEndTime(endTime);
            // 保存时间段
            boolean saved = timeSlotService.save(timeSlot);

            return saved ? R.ok().put("data", timeSlot) : R.error("创建失败");
        } catch (ParseException e) {
            return R.error("时间格式错误，请使用yyyy-MM-dd'T'HH:mm格式");
        }
    }
}