package com.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.ExamPaperEntity;
import com.entity.view.ExamPaperView;
import com.entity.vo.ExamPaperVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 试卷表 DAO
 */
public interface ExamPaperDao extends BaseMapper<ExamPaperEntity> {

	List<ExamPaperVO> selectListVO(@Param("ew") Wrapper<ExamPaperEntity> wrapper);

	ExamPaperVO selectVO(@Param("ew") Wrapper<ExamPaperEntity> wrapper);

	List<ExamPaperView> selectListView(@Param("ew") Wrapper<ExamPaperEntity> wrapper);

	// 旧版 Pagination 替换为 Page<T>
	List<ExamPaperView> selectListView(Page<ExamPaperView> page, @Param("ew") Wrapper<ExamPaperEntity> wrapper);

	ExamPaperView selectView(@Param("ew") Wrapper<ExamPaperEntity> wrapper);
}
