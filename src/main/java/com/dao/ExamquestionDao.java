package com.dao;

import com.entity.ExamQuestionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.ExamQuestionVO;
import com.entity.view.ExamQuestionView;

/**
 * 试题表
 * 
 * @author
 * @email
 * @date 2021-05-04 17:24:35
 */
public interface ExamQuestionDao extends BaseMapper<ExamQuestionEntity> {

	List<ExamQuestionVO> selectListVO(@Param("ew") Wrapper<ExamQuestionEntity> wrapper);

	ExamQuestionVO selectVO(@Param("ew") Wrapper<ExamQuestionEntity> wrapper);

	List<ExamQuestionView> selectListView(@Param("ew") Wrapper<ExamQuestionEntity> wrapper);

	List<ExamQuestionView> selectListView(Page<ExamQuestionView> page,
			@Param("ew") Wrapper<ExamQuestionEntity> wrapper);

	ExamQuestionView selectView(@Param("ew") Wrapper<ExamQuestionEntity> wrapper);

}
