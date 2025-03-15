package com.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.utils.PageUtils;
import com.entity.ExamRecordEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.ExamRecordVO;
import com.entity.view.ExamRecordView;

public interface ExamRecordService extends IService<ExamRecordEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<ExamRecordVO> selectListVO(QueryWrapper<ExamRecordEntity> wrapper);

    ExamRecordVO selectVO(QueryWrapper<ExamRecordEntity> wrapper);

    List<ExamRecordView> selectListView(QueryWrapper<ExamRecordEntity> wrapper);

    ExamRecordView selectView(QueryWrapper<ExamRecordEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, QueryWrapper<ExamRecordEntity> wrapper);

    PageUtils queryPageGroupBy(Map<String, Object> params, QueryWrapper<ExamRecordEntity> wrapper);
}
