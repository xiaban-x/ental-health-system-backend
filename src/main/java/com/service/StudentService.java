package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.entity.StudentEntity;
import com.utils.PageUtils;
import java.util.Map;
import java.util.List;

/**
 * 学生信息表 服务接口
 */
public interface StudentService extends IService<StudentEntity> {
    PageUtils queryPage(Map<String, Object> params);
    
    List<StudentEntity> selectListVO(Map<String, Object> params);
    
    StudentEntity selectVO(Map<String, Object> params);
    
    List<StudentEntity> selectListView(Map<String, Object> params);
    
    StudentEntity selectView(Map<String, Object> params);
    
    StudentEntity getByUserId(Integer userId);
}