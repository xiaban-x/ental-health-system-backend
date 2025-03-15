package com.dao;

import com.entity.ExamRecordEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.ExamRecordVO;
import com.entity.view.ExamRecordView;

/**
 * 考试记录表
 * 
 * @author
 * @email
 * @date 2021-05-04 17:24:35
 */
public interface ExamRecordDao extends BaseMapper<ExamRecordEntity> {

	List<ExamRecordVO> selectListVO(@Param("ew") Wrapper<ExamRecordEntity> wrapper);

	ExamRecordVO selectVO(@Param("ew") Wrapper<ExamRecordEntity> wrapper);

	List<ExamRecordView> selectListView(@Param("ew") Wrapper<ExamRecordEntity> wrapper);

	List<ExamRecordView> selectListView(Page<ExamRecordView> page, @Param("ew") Wrapper<ExamRecordEntity> wrapper);

	ExamRecordView selectView(@Param("ew") Wrapper<ExamRecordEntity> wrapper);

	List<ExamRecordView> selectGroupBy(Page<ExamRecordView> page, @Param("ew") Wrapper<ExamRecordEntity> wrapper);
}
