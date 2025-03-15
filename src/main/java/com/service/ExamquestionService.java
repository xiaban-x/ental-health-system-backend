package com.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utils.PageUtils;
import com.entity.ExamQuestionEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.ExamQuestionVO;
import com.entity.view.ExamQuestionView;

public interface ExamQuestionService extends IService<ExamQuestionEntity> {
    PageUtils queryPage(Map<String, Object> params);

    List<ExamQuestionVO> selectListVO(QueryWrapper<ExamQuestionEntity> wrapper);

    ExamQuestionVO selectVO(QueryWrapper<ExamQuestionEntity> wrapper);

    List<ExamQuestionView> selectListView(QueryWrapper<ExamQuestionEntity> wrapper);

    ExamQuestionView selectView(QueryWrapper<ExamQuestionEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, QueryWrapper<ExamQuestionEntity> wrapper);
}
