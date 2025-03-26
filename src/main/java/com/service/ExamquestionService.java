package com.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utils.PageUtils;
import com.entity.ExamQuestionEntity;
import java.util.Map;

public interface ExamQuestionService extends IService<ExamQuestionEntity> {
    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, QueryWrapper<ExamQuestionEntity> wrapper);
}
