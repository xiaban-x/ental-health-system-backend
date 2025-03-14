package com.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utils.PageUtils;
import com.entity.ExamrecordEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.ExamrecordVO;
import com.entity.view.ExamrecordView;

public interface ExamrecordService extends IService<ExamrecordEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    List<ExamrecordVO> selectListVO(QueryWrapper<ExamrecordEntity> wrapper);
    
    ExamrecordVO selectVO(QueryWrapper<ExamrecordEntity> wrapper);
    
    List<ExamrecordView> selectListView(QueryWrapper<ExamrecordEntity> wrapper);
    
    ExamrecordView selectView(QueryWrapper<ExamrecordEntity> wrapper);
    
    PageUtils queryPage(Map<String, Object> params, QueryWrapper<ExamrecordEntity> wrapper);
    
    PageUtils queryPageGroupBy(Map<String, Object> params, QueryWrapper<ExamrecordEntity> wrapper);
}
