package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.dao.YonghuDao;
import com.entity.YonghuEntity;
import com.service.YonghuService;
import com.entity.vo.YonghuVO;
import com.entity.view.YonghuView;

@Service("yonghuService")
public class YonghuServiceImpl extends ServiceImpl<YonghuDao, YonghuEntity> implements YonghuService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<YonghuEntity> page = this.page(
                new Page<YonghuEntity>(
                        params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
                        params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10),
                new QueryWrapper<YonghuEntity>());
        return new PageUtils(page.getRecords(), (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, QueryWrapper<YonghuEntity> wrapper) {
        Page<YonghuView> page = new Page<>(
                params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
                params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10);
        page.setRecords(baseMapper.selectListView(page, wrapper));
        return new PageUtils(page);
    }

    @Override
    public List<YonghuVO> selectListVO(QueryWrapper<YonghuEntity> wrapper) {
        return baseMapper.selectListVO(wrapper);
    }

    @Override
    public YonghuVO selectVO(QueryWrapper<YonghuEntity> wrapper) {
        return baseMapper.selectVO(wrapper);
    }

    @Override
    public List<YonghuView> selectListView(QueryWrapper<YonghuEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public YonghuView selectView(QueryWrapper<YonghuEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }
}
