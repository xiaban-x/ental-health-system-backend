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
import com.dao.DoctorDao;
import com.entity.DoctorEntity;
import com.service.DoctorService;

/**
 * 医生信息表 服务实现类
 */
@Service("doctorService")
public class DoctorServiceImpl extends ServiceImpl<DoctorDao, DoctorEntity> implements DoctorService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<DoctorEntity> page = this.page(
                new Page<DoctorEntity>(
                        params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
                        params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10),
                new QueryWrapper<DoctorEntity>());
        return new PageUtils(page.getRecords(), (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
    }

    @Override
    public List<DoctorEntity> selectListVO(Map<String, Object> params) {
        return baseMapper.selectListVO(params);
    }

    @Override
    public DoctorEntity selectVO(Map<String, Object> params) {
        return baseMapper.selectVO(params);
    }

    @Override
    public List<DoctorEntity> selectListView(Map<String, Object> params) {
        return baseMapper.selectListView(params);
    }

    @Override
    public DoctorEntity selectView(Map<String, Object> params) {
        return baseMapper.selectView(params);
    }

    @Override
    public DoctorEntity getByUserId(Integer userId) {
        return baseMapper.selectByUserId(userId);
    }
}