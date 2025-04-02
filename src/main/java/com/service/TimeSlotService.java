package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.entity.TimeSlotEntity;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.utils.PageUtils;

import java.util.Map;

/**
 * 时间段服务接口
 */
public interface TimeSlotService extends IService<TimeSlotEntity> {
    /**
     * 分页查询
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 条件分页查询
     */
    PageUtils queryPage(Map<String, Object> params, QueryWrapper<TimeSlotEntity> wrapper);

}