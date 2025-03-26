package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utils.PageUtils;

import com.dao.ExamPaperDao;
import com.entity.ExamPaperEntity;
import com.service.ExamPaperService;
import com.entity.vo.ExamPaperVO;
import com.entity.view.ExamPaperView;

@Service("examPaperService")
public class ExamPaperServiceImpl extends ServiceImpl<ExamPaperDao, ExamPaperEntity> implements ExamPaperService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		IPage<ExamPaperEntity> page = this.page(
				new Page<ExamPaperEntity>(
						params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
						params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10),
				new QueryWrapper<ExamPaperEntity>());
		return new PageUtils(page.getRecords(), (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
	}

	@Override
	public PageUtils queryPage(Map<String, Object> params, QueryWrapper<ExamPaperEntity> wrapper) {
		// 创建分页对象
		Page<ExamPaperEntity> page = new Page<>(
				params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
				params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10);
		
		// 使用page方法进行分页查询，这样会在SQL中添加LIMIT子句
		IPage<ExamPaperEntity> iPage = this.page(page, wrapper);
		
		// 返回分页结果
		return new PageUtils(iPage.getRecords(), (int) iPage.getTotal(), (int) iPage.getSize(), (int) iPage.getCurrent());
	}

	@Override
	public List<ExamPaperVO> selectListVO(QueryWrapper<ExamPaperEntity> wrapper) {
		return baseMapper.selectListVO(wrapper);
	}

	@Override
	public ExamPaperVO selectVO(QueryWrapper<ExamPaperEntity> wrapper) {
		return baseMapper.selectVO(wrapper);
	}

	@Override
	public List<ExamPaperView> selectListView(QueryWrapper<ExamPaperEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public ExamPaperView selectView(QueryWrapper<ExamPaperEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}
}
