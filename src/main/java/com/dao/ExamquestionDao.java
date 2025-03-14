package com.dao;

import com.entity.ExamquestionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.ExamquestionVO;
import com.entity.view.ExamquestionView;

/**
 * 试题表
 * 
 * @author
 * @email
 * @date 2021-05-04 17:24:35
 */
public interface ExamquestionDao extends BaseMapper<ExamquestionEntity> {

	List<ExamquestionVO> selectListVO(@Param("ew") Wrapper<ExamquestionEntity> wrapper);

	ExamquestionVO selectVO(@Param("ew") Wrapper<ExamquestionEntity> wrapper);

	List<ExamquestionView> selectListView(@Param("ew") Wrapper<ExamquestionEntity> wrapper);

	List<ExamquestionView> selectListView(Page<ExamquestionView> page,
			@Param("ew") Wrapper<ExamquestionEntity> wrapper);

	ExamquestionView selectView(@Param("ew") Wrapper<ExamquestionEntity> wrapper);

}
