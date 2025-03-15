package com.dao;

import com.entity.UserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.UserVO;
import com.entity.view.UserView;

/**
 * 用户
 * 
 * @author
 * @email
 * @date 2021-05-04 17:24:35
 */
public interface UserDao extends BaseMapper<UserEntity> {

	List<UserVO> selectListVO(@Param("ew") Wrapper<UserEntity> wrapper);

	UserVO selectVO(@Param("ew") Wrapper<UserEntity> wrapper);

	List<UserView> selectListView(@Param("ew") Wrapper<UserEntity> wrapper);

	List<UserView> selectListView(Page<UserView> page, @Param("ew") Wrapper<UserEntity> wrapper);

	UserView selectView(@Param("ew") Wrapper<UserEntity> wrapper);

}
