package com.dao;

import com.entity.DoctorEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 医生信息表 Dao 接口
 */
@Mapper
public interface DoctorDao extends BaseMapper<DoctorEntity> {
    List<DoctorEntity> selectListVO(@Param("ew") Map<String, Object> params);

    DoctorEntity selectVO(@Param("ew") Map<String, Object> params);

    List<DoctorEntity> selectListView(@Param("ew") Map<String, Object> params);

    DoctorEntity selectView(@Param("ew") Map<String, Object> params);

    DoctorEntity selectByUserId(Integer userId);
}