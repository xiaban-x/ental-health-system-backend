package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.entity.CounselorEntity;
import com.utils.PageUtils;
import java.util.Map;

public interface CounselorService extends IService<CounselorEntity> {
    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * 根据用户ID获取咨询师/教师信息
     */
    CounselorEntity getByUserId(Integer userId);
}