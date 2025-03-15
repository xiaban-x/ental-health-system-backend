package com.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utils.PageUtils;
import com.entity.ExamPaperEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.ExamPaperVO;
import com.entity.view.ExamPaperView;

public interface ExamPaperService extends IService<ExamPaperEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<ExamPaperVO> selectListVO(QueryWrapper<ExamPaperEntity> wrapper);

    ExamPaperVO selectVO(QueryWrapper<ExamPaperEntity> wrapper);

    List<ExamPaperView> selectListView(QueryWrapper<ExamPaperEntity> wrapper);

    ExamPaperView selectView(QueryWrapper<ExamPaperEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, QueryWrapper<ExamPaperEntity> wrapper);
}
