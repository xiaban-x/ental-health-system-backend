
package com.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dao.UserDao;
import com.entity.UserEntity;
import com.service.UserService;
import com.utils.PageUtils;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		IPage<UserEntity> page = this.page(
				new Page<UserEntity>(
						params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
						params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10),
				new QueryWrapper<UserEntity>());
		return new PageUtils(page.getRecords(), (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
	}

	@Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<UserEntity> wrapper) {
		Page<UserEntity> page = new Page<>(
				params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
				params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10);
		page.setRecords(baseMapper.selectListView(page, wrapper));
		return new PageUtils(page);
	}

	@Override
	public List<UserEntity> selectListView(Wrapper<UserEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}
}
