package com.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entity.CounselorEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 咨询师Mapper接口
 */
@Mapper
public interface CounselorDao extends BaseMapper<CounselorEntity> {
}