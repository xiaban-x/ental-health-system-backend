package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;

import com.dao.ExampaperDao;
import com.entity.ExampaperEntity;
import com.service.ExampaperService;
import com.entity.vo.ExampaperVO;
import com.entity.view.ExampaperView;

@Service("exampaperService")
public class ExampaperServiceImpl extends ServiceImpl<ExampaperDao, ExampaperEntity> implements ExampaperService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		IPage<ExampaperEntity> page = this.page(
				new Page<ExampaperEntity>(
						params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
						params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10),
				new QueryWrapper<ExampaperEntity>());
		return new PageUtils(page.getRecords(), (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
	}

	@Override
	public PageUtils queryPage(Map<String, Object> params, QueryWrapper<ExampaperEntity> wrapper) {
		Page<ExampaperView> page = new Page<>(
				params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
				params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10);
		page.setRecords(baseMapper.selectListView((Page<ExampaperView>) page, wrapper));
		return new PageUtils(page);
	}

	@Override
	public List<ExampaperVO> selectListVO(QueryWrapper<ExampaperEntity> wrapper) {
		return baseMapper.selectListVO(wrapper);
	}

	@Override
	public ExampaperVO selectVO(QueryWrapper<ExampaperEntity> wrapper) {
		return baseMapper.selectVO(wrapper);
	}

	@Override
	public List<ExampaperView> selectListView(QueryWrapper<ExampaperEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public ExampaperView selectView(QueryWrapper<ExampaperEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}
}
