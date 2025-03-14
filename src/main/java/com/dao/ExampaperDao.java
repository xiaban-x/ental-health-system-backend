package com.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.ExampaperEntity;
import com.entity.view.ExampaperView;
import com.entity.vo.ExampaperVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 试卷表 DAO
 */
public interface ExampaperDao extends BaseMapper<ExampaperEntity> {

	List<ExampaperVO> selectListVO(@Param("ew") Wrapper<ExampaperEntity> wrapper);

	ExampaperVO selectVO(@Param("ew") Wrapper<ExampaperEntity> wrapper);

	List<ExampaperView> selectListView(@Param("ew") Wrapper<ExampaperEntity> wrapper);

	// 旧版 Pagination 替换为 Page<T>
	List<ExampaperView> selectListView(Page<ExampaperView> page, @Param("ew") Wrapper<ExampaperEntity> wrapper);

	ExampaperView selectView(@Param("ew") Wrapper<ExampaperEntity> wrapper);
}
