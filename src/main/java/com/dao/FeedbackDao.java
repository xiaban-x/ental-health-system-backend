package com.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entity.FeedbackEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 反馈Mapper接口
 */
@Mapper
public interface FeedbackDao extends BaseMapper<FeedbackEntity> {
}