package com.dao;

import com.entity.ExamrecordEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.ExamrecordVO;
import com.entity.view.ExamrecordView;

/**
 * 考试记录表
 * 
 * @author
 * @email
 * @date 2021-05-04 17:24:35
 */
public interface ExamrecordDao extends BaseMapper<ExamrecordEntity> {

	List<ExamrecordVO> selectListVO(@Param("ew") Wrapper<ExamrecordEntity> wrapper);

	ExamrecordVO selectVO(@Param("ew") Wrapper<ExamrecordEntity> wrapper);

	List<ExamrecordView> selectListView(@Param("ew") Wrapper<ExamrecordEntity> wrapper);

	List<ExamrecordView> selectListView(Page<ExamrecordView> page, @Param("ew") Wrapper<ExamrecordEntity> wrapper);

	ExamrecordView selectView(@Param("ew") Wrapper<ExamrecordEntity> wrapper);

	List<ExamrecordView> selectGroupBy(Page<ExamrecordView> page, @Param("ew") Wrapper<ExamrecordEntity> wrapper);
}
