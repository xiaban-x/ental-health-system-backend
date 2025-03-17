package com.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entity.AppointmentEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 预约Mapper接口
 */
@Mapper
public interface AppointmentDao extends BaseMapper<AppointmentEntity> {
}