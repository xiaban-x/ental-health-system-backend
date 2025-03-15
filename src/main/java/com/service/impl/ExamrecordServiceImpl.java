package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utils.PageUtils;

import com.dao.ExamRecordDao;
import com.entity.ExamRecordEntity;
import com.service.ExamRecordService;
import com.entity.vo.ExamRecordVO;
import com.entity.view.ExamRecordView;

@Service("examrecordService")
public class ExamRecordServiceImpl extends ServiceImpl<ExamRecordDao, ExamRecordEntity> implements ExamRecordService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		IPage<ExamRecordEntity> page = this.page(
				new Page<ExamRecordEntity>(
						params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
						params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10),
				new QueryWrapper<ExamRecordEntity>());
		return new PageUtils(page.getRecords(), (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
	}

	@Override
	public PageUtils queryPage(Map<String, Object> params, QueryWrapper<ExamRecordEntity> wrapper) {
		Page<ExamRecordView> page = new Page<>(
				params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
				params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10);
		page.setRecords(baseMapper.selectListView((Page<ExamRecordView>) page, wrapper));
		return new PageUtils(page);
	}

	@Override
	public PageUtils queryPageGroupBy(Map<String, Object> params, QueryWrapper<ExamRecordEntity> wrapper) {
		Page<ExamRecordView> page = new Page<>(
				params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
				params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10);
		page.setRecords(baseMapper.selectGroupBy((Page<ExamRecordView>) page, wrapper));
		return new PageUtils(page);
	}

	@Override
	public List<ExamRecordVO> selectListVO(QueryWrapper<ExamRecordEntity> wrapper) {
		return baseMapper.selectListVO(wrapper);
	}

	@Override
	public ExamRecordVO selectVO(QueryWrapper<ExamRecordEntity> wrapper) {
		return baseMapper.selectVO(wrapper);
	}

	@Override
	public List<ExamRecordView> selectListView(QueryWrapper<ExamRecordEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public ExamRecordView selectView(QueryWrapper<ExamRecordEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}
}
