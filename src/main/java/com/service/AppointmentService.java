package com.service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.entity.AppointmentEntity;
import com.utils.PageUtils;

/**
 * 预约服务接口
 */
public interface AppointmentService extends IService<AppointmentEntity> {
    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, QueryWrapper<AppointmentEntity> wrapper);
}