package com.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.CounselorEntity;
import com.entity.TimeSlotEntity;
import com.service.CounselorService;
import com.service.TimeSlotService;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Date;
import java.util.List;

/**
 * 咨询师相关接口
 */
@RestController
@RequestMapping("/counselor")
@Tag(name = "咨询师管理", description = "管理咨询师信息的相关接口")
public class CounselorController {

    @Autowired
    private CounselorService counselorService;

    @Autowired
    private TimeSlotService timeSlotService;

    /**
     * 获取咨询师列表
     */
    @Operation(summary = "获取咨询师列表", description = "获取所有可用的咨询师列表")
    @GetMapping("/list")
    public R list() {
        QueryWrapper<CounselorEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1); // 只获取状态为可用的咨询师
        queryWrapper.orderByAsc("id");
        List<CounselorEntity> list = counselorService.list(queryWrapper);
        return R.ok().put("data", list);
    }

    /**
     * 获取咨询师详情
     */
    @Operation(summary = "获取咨询师详情", description = "根据ID获取咨询师的详细信息")
    @Parameter(name = "id", description = "咨询师ID", required = true)
    @GetMapping("/{id}")
    public R info(@PathVariable("id") Long id) {
        CounselorEntity counselor = counselorService.getById(id);
        if (counselor == null) {
            return R.error("咨询师不存在");
        }
        return R.ok().put("data", counselor);
    }

    /**
     * 获取咨询师可用时间段
     */
    @Operation(summary = "获取咨询师可用时间段", description = "获取指定咨询师的所有可用预约时间段")
    @Parameter(name = "id", description = "咨询师ID", required = true)
    @GetMapping("/{id}/time-slots")
    public R timeSlots(@PathVariable("id") Long id) {
        QueryWrapper<TimeSlotEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("counselor_id", id);
        queryWrapper.ge("start_time", new Date()); // 只获取当前时间之后的时间段
        queryWrapper.orderByAsc("start_time");
        List<TimeSlotEntity> list = timeSlotService.list(queryWrapper);
        return R.ok().put("data", list);
    }
}