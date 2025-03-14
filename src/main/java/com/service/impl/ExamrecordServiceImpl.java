package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utils.PageUtils;

import com.dao.ExamrecordDao;
import com.entity.ExamrecordEntity;
import com.service.ExamrecordService;
import com.entity.vo.ExamrecordVO;
import com.entity.view.ExamrecordView;

@Service("examrecordService")
public class ExamrecordServiceImpl extends ServiceImpl<ExamrecordDao, ExamrecordEntity> implements ExamrecordService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		IPage<ExamrecordEntity> page = this.page(
				new Page<ExamrecordEntity>(
						params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
						params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10),
				new QueryWrapper<ExamrecordEntity>());
		return new PageUtils(page.getRecords(), (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
	}

	@Override
	public PageUtils queryPage(Map<String, Object> params, QueryWrapper<ExamrecordEntity> wrapper) {
		Page<ExamrecordView> page = new Page<>(
				params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
				params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10);
		page.setRecords(baseMapper.selectListView((Page<ExamrecordView>) page, wrapper));
		return new PageUtils(page);
	}

	@Override
	public PageUtils queryPageGroupBy(Map<String, Object> params, QueryWrapper<ExamrecordEntity> wrapper) {
		Page<ExamrecordView> page = new Page<>(
				params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
				params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10);
		page.setRecords(baseMapper.selectGroupBy((Page<ExamrecordView>) page, wrapper));
		return new PageUtils(page);
	}

	@Override
	public List<ExamrecordVO> selectListVO(QueryWrapper<ExamrecordEntity> wrapper) {
		return baseMapper.selectListVO(wrapper);
	}

	@Override
	public ExamrecordVO selectVO(QueryWrapper<ExamrecordEntity> wrapper) {
		return baseMapper.selectVO(wrapper);
	}

	@Override
	public List<ExamrecordView> selectListView(QueryWrapper<ExamrecordEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public ExamrecordView selectView(QueryWrapper<ExamrecordEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}
}
