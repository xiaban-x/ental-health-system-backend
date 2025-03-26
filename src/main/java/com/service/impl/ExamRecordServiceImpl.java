package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utils.PageUtils;

import com.dao.ExamRecordDao;
import com.entity.ExamRecordEntity;
import com.service.ExamRecordService;

@Service("examRecordService")
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
		// 创建分页对象
		Page<ExamRecordEntity> page = new Page<>(
				params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
				params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10);
		
		// 使用page方法进行分页查询，这样会在SQL中添加LIMIT子句
		IPage<ExamRecordEntity> iPage = this.page(page, wrapper);
		
		// 返回分页结果
		return new PageUtils(iPage.getRecords(), (int) iPage.getTotal(), (int) iPage.getSize(), (int) iPage.getCurrent());
	}
}
