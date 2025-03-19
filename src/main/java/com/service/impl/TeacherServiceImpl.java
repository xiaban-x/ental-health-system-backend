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
import com.dao.TeacherDao;
import com.entity.TeacherEntity;
import com.service.TeacherService;

/**
 * 教师信息表 服务实现类
 */
@Service("teacherService")
public class TeacherServiceImpl extends ServiceImpl<TeacherDao, TeacherEntity> implements TeacherService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TeacherEntity> page = this.page(
                new Page<TeacherEntity>(
                        params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
                        params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10),
                new QueryWrapper<TeacherEntity>());
        return new PageUtils(page.getRecords(), (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
    }

    @Override
    public List<TeacherEntity> selectListVO(Map<String, Object> params) {
        return baseMapper.selectListVO(params);
    }

    @Override
    public TeacherEntity selectVO(Map<String, Object> params) {
        return baseMapper.selectVO(params);
    }

    @Override
    public List<TeacherEntity> selectListView(Map<String, Object> params) {
        return baseMapper.selectListView(params);
    }

    @Override
    public TeacherEntity selectView(Map<String, Object> params) {
        return baseMapper.selectView(params);
    }

    @Override
    public TeacherEntity getByUserId(Integer userId) {
        return baseMapper.selectByUserId(userId);
    }
}