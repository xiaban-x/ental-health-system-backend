package com.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utils.PageUtils;
import com.entity.ExampaperEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.ExampaperVO;
import com.entity.view.ExampaperView;

public interface ExampaperService extends IService<ExampaperEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    List<ExampaperVO> selectListVO(QueryWrapper<ExampaperEntity> wrapper);
    
    ExampaperVO selectVO(QueryWrapper<ExampaperEntity> wrapper);
    
    List<ExampaperView> selectListView(QueryWrapper<ExampaperEntity> wrapper);
    
    ExampaperView selectView(QueryWrapper<ExampaperEntity> wrapper);
    
    PageUtils queryPage(Map<String, Object> params, QueryWrapper<ExampaperEntity> wrapper);
}
