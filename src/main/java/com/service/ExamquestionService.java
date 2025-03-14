package com.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utils.PageUtils;
import com.entity.ExamquestionEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.ExamquestionVO;
import com.entity.view.ExamquestionView;

public interface ExamquestionService extends IService<ExamquestionEntity> {
    PageUtils queryPage(Map<String, Object> params);
    
    List<ExamquestionVO> selectListVO(QueryWrapper<ExamquestionEntity> wrapper);
    
    ExamquestionVO selectVO(QueryWrapper<ExamquestionEntity> wrapper);
    
    List<ExamquestionView> selectListView(QueryWrapper<ExamquestionEntity> wrapper);
    
    ExamquestionView selectView(QueryWrapper<ExamquestionEntity> wrapper);
    
    PageUtils queryPage(Map<String, Object> params, QueryWrapper<ExamquestionEntity> wrapper);
}
