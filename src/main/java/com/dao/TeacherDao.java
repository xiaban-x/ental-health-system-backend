package com.dao;

import com.entity.TeacherEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 教师信息表 Dao 接口
 */
@Mapper
public interface TeacherDao extends BaseMapper<TeacherEntity> {
    List<TeacherEntity> selectListVO(@Param("ew") Map<String, Object> params);
    
    TeacherEntity selectVO(@Param("ew") Map<String, Object> params);
    
    List<TeacherEntity> selectListView(@Param("ew") Map<String, Object> params);
    
    TeacherEntity selectView(@Param("ew") Map<String, Object> params);
    
    TeacherEntity selectByUserId(Integer userId);
}