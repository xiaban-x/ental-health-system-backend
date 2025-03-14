package com.dao;

import com.entity.YonghuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.YonghuVO;
import com.entity.view.YonghuView;

/**
 * 用户
 * 
 * @author
 * @email
 * @date 2021-05-04 17:24:35
 */
public interface YonghuDao extends BaseMapper<YonghuEntity> {

	List<YonghuVO> selectListVO(@Param("ew") Wrapper<YonghuEntity> wrapper);

	YonghuVO selectVO(@Param("ew") Wrapper<YonghuEntity> wrapper);

	List<YonghuView> selectListView(@Param("ew") Wrapper<YonghuEntity> wrapper);

	List<YonghuView> selectListView(Page<YonghuView> page, @Param("ew") Wrapper<YonghuEntity> wrapper);

	YonghuView selectView(@Param("ew") Wrapper<YonghuEntity> wrapper);

}
