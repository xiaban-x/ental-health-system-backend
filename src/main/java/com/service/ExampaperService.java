package com.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utils.PageUtils;
import com.entity.ExamPaperEntity;
import java.util.Map;

public interface ExamPaperService extends IService<ExamPaperEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, QueryWrapper<ExamPaperEntity> wrapper);
}
