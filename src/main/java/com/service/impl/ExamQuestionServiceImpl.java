package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utils.PageUtils;

import com.dao.ExamQuestionDao;
import com.entity.ExamQuestionEntity;
import com.service.ExamQuestionService;

@Service("examQuestionService")
public class ExamQuestionServiceImpl extends ServiceImpl<ExamQuestionDao, ExamQuestionEntity>
        implements ExamQuestionService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ExamQuestionEntity> page = this.page(
                new Page<ExamQuestionEntity>(
                        params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
                        params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10),
                new QueryWrapper<ExamQuestionEntity>());
        return new PageUtils(page.getRecords(), (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, QueryWrapper<ExamQuestionEntity> wrapper) {
        // 创建分页对象
        Page<ExamQuestionEntity> page = new Page<>(
                params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
                params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10);

        // 使用page方法进行分页查询，这样会在SQL中添加LIMIT子句
        IPage<ExamQuestionEntity> iPage = this.page(page, wrapper);

        // 返回分页结果
        return new PageUtils(iPage.getRecords(), (int) iPage.getTotal(), (int) iPage.getSize(),
                (int) iPage.getCurrent());
    }
}
