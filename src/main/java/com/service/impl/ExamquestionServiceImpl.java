package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utils.PageUtils;

import com.dao.ExamquestionDao;
import com.entity.ExamquestionEntity;
import com.service.ExamquestionService;
import com.entity.vo.ExamquestionVO;
import com.entity.view.ExamquestionView;

@Service("examquestionService")
public class ExamquestionServiceImpl extends ServiceImpl<ExamquestionDao, ExamquestionEntity> implements ExamquestionService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ExamquestionEntity> page = this.page(
                new Page<ExamquestionEntity>(
                        params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
                        params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10),
                new QueryWrapper<ExamquestionEntity>());
        return new PageUtils(page.getRecords(), (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, QueryWrapper<ExamquestionEntity> wrapper) {
        Page<ExamquestionView> page = new Page<>(
                params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
                params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10);
        page.setRecords(baseMapper.selectListView((Page<ExamquestionView>) page, wrapper));
        return new PageUtils(page);
    }

    @Override
    public List<ExamquestionVO> selectListVO(QueryWrapper<ExamquestionEntity> wrapper) {
        return baseMapper.selectListVO(wrapper);
    }

    @Override
    public ExamquestionVO selectVO(QueryWrapper<ExamquestionEntity> wrapper) {
        return baseMapper.selectVO(wrapper);
    }

    @Override
    public List<ExamquestionView> selectListView(QueryWrapper<ExamquestionEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public ExamquestionView selectView(QueryWrapper<ExamquestionEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }
}
