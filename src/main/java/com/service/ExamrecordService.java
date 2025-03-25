package com.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utils.PageUtils;
import com.entity.ExamRecordEntity;
import java.util.Map;

public interface ExamRecordService extends IService<ExamRecordEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, QueryWrapper<ExamRecordEntity> wrapper);
}
