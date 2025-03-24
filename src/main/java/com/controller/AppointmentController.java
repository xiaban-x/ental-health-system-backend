package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.entity.AppointmentEntity;
import com.entity.CounselorEntity;
import com.entity.TimeSlotEntity;
import com.entity.UserEntity;
import com.service.AppointmentService;
import com.service.CounselorService;
import com.service.TimeSlotService;
import com.service.UserService;
import com.utils.R;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 预约相关接口
 */
@RestController
@RequestMapping("/api/v1/appointment")
@Tag(name = "预约管理", description = "管理咨询预约的相关接口")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private TimeSlotService timeSlotService;

    @Autowired
    private UserService userService;

    @Autowired
    private CounselorService counselorService;

    /**
     * 创建预约
     */
    @Operation(summary = "创建预约", description = "创建新的咨询预约")
    @Parameters({
            @Parameter(name = "counselorId", description = "咨询师ID", required = true),
            @Parameter(name = "timeSlotId", description = "时间段ID", required = true),
            @Parameter(name = "reason", description = "预约原因", required = true)
    })
    @PostMapping("/create")
    @Transactional
    public R create(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        // 从token中获取用户信息
        Integer userId = (Integer) request.getAttribute("userId");
        UserEntity user = userService.getById(userId);
        if (user == null) {
            return R.error("用户不存在");
        }

        Integer counselorId = Integer.parseInt(params.get("counselorId").toString());
        Integer timeSlotId = Integer.parseInt(params.get("timeSlotId").toString());
        String reason = params.get("reason").toString();

        // 检查时间段是否可用
        TimeSlotEntity timeSlot = timeSlotService.getById(timeSlotId);
        if (timeSlot == null) {
            return R.error("时间段不存在");
        }
        if (!"available".equals(timeSlot.getStatus())) {
            return R.error("该时间段已被预约");
        }

        // 更新时间段状态
        timeSlot.setStatus("booked");
        timeSlotService.updateById(timeSlot);

        // 创建预约
        AppointmentEntity appointment = new AppointmentEntity();
        // 使用雪花算法生成ID
        appointment.setId((int) IdWorker.getId());
        appointment.setUserId(userId);
        appointment.setUsername(user.getUsername());
        appointment.setCounselorId(counselorId);
        // 获取咨询师姓名
        CounselorEntity counselor = counselorService.getById(counselorId);
        if (counselor != null) {
            appointment.setCounselorName(user.getName());
        }
        appointment.setTimeSlotId(timeSlotId);
        appointment.setStartTime(timeSlot.getStartTime());
        appointment.setEndTime(timeSlot.getEndTime());
        appointment.setStatus("pending");
        appointment.setReason(reason);

        try {
            boolean success = appointmentService.save(appointment);
            if (!success) {
                // 如果保存失败，回滚时间段状态
                timeSlot.setStatus("available");
                timeSlotService.updateById(timeSlot);
                return R.error("预约创建失败");
            }
        } catch (Exception e) {
            // 发生异常时，回滚时间段状态
            timeSlot.setStatus("available");
            timeSlotService.updateById(timeSlot);
            return R.error("预约创建失败：" + e.getMessage());
        }

        return R.ok();
    }

    /**
     * 获取我的预约列表
     */
    @Operation(summary = "获取我的预约列表", description = "获取当前用户的所有预约记录")
    @GetMapping("/my-appointments")
    public R myAppointments(HttpServletRequest request) {
        // 从token中获取用户信息
        int userId = (int) request.getAttribute("userId");

        QueryWrapper<AppointmentEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("created_at");
        List<AppointmentEntity> list = appointmentService.list(queryWrapper);
        return R.ok().put("data", list);
    }

    /**
     * 取消预约
     */
    @Operation(summary = "取消预约", description = "取消指定的预约记录")
    @Parameter(name = "id", description = "预约ID", required = true)
    @PostMapping("/{id}/cancel")
    @Transactional
    public R cancel(@PathVariable("id") Integer id, HttpServletRequest request) {
        // 从token中获取用户信息
        Integer userId = (Integer) request.getAttribute("userId");

        AppointmentEntity appointment = appointmentService.getById(id);
        if (appointment == null) {
            return R.error("预约不存在");
        }

        // 检查是否是自己的预约
        if (!userId.equals(appointment.getUserId())) {
            return R.error("无权操作此预约");
        }

        // 检查预约状态
        if ("completed".equals(appointment.getStatus()) || "cancelled".equals(appointment.getStatus())) {
            return R.error("该预约已完成或已取消，无法取消");
        }

        // 更新预约状态
        appointment.setStatus("cancelled");
        appointmentService.updateById(appointment);

        // 更新时间段状态
        TimeSlotEntity timeSlot = timeSlotService.getById(appointment.getTimeSlotId());
        if (timeSlot != null) {
            timeSlot.setStatus("available");
            timeSlotService.updateById(timeSlot);
        }

        return R.ok();
    }

    /**
     * 咨询师审核预约
     */
    @Operation(summary = "审核预约", description = "咨询师审核预约申请")
    @Parameters({
            @Parameter(name = "id", description = "预约ID", required = true),
            @Parameter(name = "status", description = "预约状态(approved/rejected)", required = true),
            @Parameter(name = "comment", description = "审核意见", required = false)
    })
    @PostMapping("/{id}/review")
    @Transactional
    public R reviewAppointment(@PathVariable("id") Integer id, @RequestBody Map<String, String> params,
            HttpServletRequest request) {
        // 从token中获取用户信息
        Integer userId = (Integer) request.getAttribute("userId");

        // 检查用户是否是咨询师
        QueryWrapper<CounselorEntity> counselorQuery = new QueryWrapper<>();
        counselorQuery.eq("user_id", userId);
        CounselorEntity counselor = counselorService.getOne(counselorQuery);

        if (counselor == null) {
            return R.error("您不是咨询师，无权审核预约");
        }

        // 获取预约记录
        AppointmentEntity appointment = appointmentService.getById(id);
        if (appointment == null) {
            return R.error("预约不存在");
        }

        // 检查是否是分配给该咨询师的预约
        if (!counselor.getId().equals(appointment.getCounselorId())) {
            return R.error("您无权审核此预约");
        }

        // 检查预约状态是否为待审核
        if (!"available".equals(appointment.getStatus())) {
            return R.error("该预约已被审核或已取消，无法再次审核");
        }

        // 获取审核结果
        String status = params.get("status");
        if (status == null || (!status.equals("approved") && !status.equals("rejected"))) {
            return R.error("无效的审核状态，必须为approved或rejected");
        }

        // 获取审核意见
        String notes = params.get("notes");

        // 更新预约状态
        appointment.setStatus(status);
        appointment.setNotes(notes);
        appointmentService.updateById(appointment);

        // 如果拒绝预约，将时间段状态改回可用
        if ("rejected".equals(status)) {
            TimeSlotEntity timeSlot = timeSlotService.getById(appointment.getTimeSlotId());
            if (timeSlot != null) {
                timeSlot.setStatus("available");
                timeSlotService.updateById(timeSlot);
            }
        }

        return R.ok();
    }

    /**
     * 获取咨询师的预约列表
     */
    @Operation(summary = "获取咨询师预约列表", description = "获取当前咨询师的所有预约记录")
    @GetMapping("/counselor-appointments")
    public R counselorAppointments(HttpServletRequest request) {
        // 从token中获取用户信息
        Integer userId = (Integer) request.getAttribute("userId");

        // 检查用户是否是咨询师
        QueryWrapper<CounselorEntity> counselorQuery = new QueryWrapper<>();
        counselorQuery.eq("user_id", userId);
        CounselorEntity counselor = counselorService.getOne(counselorQuery);

        if (counselor == null) {
            return R.error("您不是咨询师");
        }

        // 查询该咨询师的所有预约
        QueryWrapper<AppointmentEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("counselor_id", counselor.getId());
        queryWrapper.orderByDesc("created_at");
        List<AppointmentEntity> list = appointmentService.list(queryWrapper);

        return R.ok().put("data", list);
    }
}