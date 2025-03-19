package com.dao;

import com.entity.StudentEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 学生信息表 Dao 接口
 */
@Mapper
public interface StudentDao extends BaseMapper<StudentEntity> {
    List<StudentEntity> selectListVO(@Param("ew") Map<String, Object> params);

    StudentEntity selectVO(@Param("ew") Map<String, Object> params);

    List<StudentEntity> selectListView(@Param("ew") Map<String, Object> params);

    StudentEntity selectView(@Param("ew") Map<String, Object> params);

    StudentEntity selectByUserId(Integer userId);
}