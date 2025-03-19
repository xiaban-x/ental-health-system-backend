package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.entity.DoctorEntity;
import com.utils.PageUtils;
import java.util.Map;
import java.util.List;

/**
 * 医生信息表 服务接口
 */
public interface DoctorService extends IService<DoctorEntity> {
    PageUtils queryPage(Map<String, Object> params);
    
    List<DoctorEntity> selectListVO(Map<String, Object> params);
    
    DoctorEntity selectVO(Map<String, Object> params);
    
    List<DoctorEntity> selectListView(Map<String, Object> params);
    
    DoctorEntity selectView(Map<String, Object> params);
    
    DoctorEntity getByUserId(Integer userId);
}