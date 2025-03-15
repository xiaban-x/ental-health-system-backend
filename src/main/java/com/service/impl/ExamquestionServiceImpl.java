package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utils.PageUtils;

import com.dao.ExamQuestionDao;
import com.entity.ExamQuestionEntity;
import com.service.ExamQuestionService;
import com.entity.vo.ExamQuestionVO;
import com.entity.view.ExamQuestionView;

@Service("examquestionService")
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
        Page<ExamQuestionView> page = new Page<>(
                params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
                params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10);
        page.setRecords(baseMapper.selectListView((Page<ExamQuestionView>) page, wrapper));
        return new PageUtils(page);
    }

    @Override
    public List<ExamQuestionVO> selectListVO(QueryWrapper<ExamQuestionEntity> wrapper) {
        return baseMapper.selectListVO(wrapper);
    }

    @Override
    public ExamQuestionVO selectVO(QueryWrapper<ExamQuestionEntity> wrapper) {
        return baseMapper.selectVO(wrapper);
    }

    @Override
    public List<ExamQuestionView> selectListView(QueryWrapper<ExamQuestionEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public ExamQuestionView selectView(QueryWrapper<ExamQuestionEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }
}
