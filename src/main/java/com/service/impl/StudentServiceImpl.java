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
import com.dao.StudentDao;
import com.entity.StudentEntity;
import com.service.StudentService;

/**
 * 学生信息表 服务实现类
 */
@Service("studentService")
public class StudentServiceImpl extends ServiceImpl<StudentDao, StudentEntity> implements StudentService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<StudentEntity> page = this.page(
                new Page<StudentEntity>(
                        params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
                        params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10),
                new QueryWrapper<StudentEntity>());
        return new PageUtils(page.getRecords(), (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
    }

    @Override
    public List<StudentEntity> selectListVO(Map<String, Object> params) {
        return baseMapper.selectListVO(params);
    }

    @Override
    public StudentEntity selectVO(Map<String, Object> params) {
        return baseMapper.selectVO(params);
    }

    @Override
    public List<StudentEntity> selectListView(Map<String, Object> params) {
        return baseMapper.selectListView(params);
    }

    @Override
    public StudentEntity selectView(Map<String, Object> params) {
        return baseMapper.selectView(params);
    }

    @Override
    public StudentEntity getByUserId(Integer userId) {
        return baseMapper.selectByUserId(userId);
    }
}