package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.entity.TeacherEntity;
import com.utils.PageUtils;
import java.util.Map;
import java.util.List;

/**
 * 教师信息表 服务接口
 */
public interface TeacherService extends IService<TeacherEntity> {
    PageUtils queryPage(Map<String, Object> params);
    
    List<TeacherEntity> selectListVO(Map<String, Object> params);
    
    TeacherEntity selectVO(Map<String, Object> params);
    
    List<TeacherEntity> selectListView(Map<String, Object> params);
    
    TeacherEntity selectView(Map<String, Object> params);
    
    TeacherEntity getByUserId(Integer userId);
}