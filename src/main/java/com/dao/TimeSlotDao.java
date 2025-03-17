package com.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entity.TimeSlotEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 时间段Mapper接口
 */
@Mapper
public interface TimeSlotDao extends BaseMapper<TimeSlotEntity> {
}