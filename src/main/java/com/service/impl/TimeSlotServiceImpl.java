package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.entity.TimeSlotEntity;
import com.dao.TimeSlotDao;
import com.service.TimeSlotService;
import org.springframework.stereotype.Service;
import com.utils.PageUtils;
import java.util.Map;

/**
 * 时间段服务实现类
 */
@Service
public class TimeSlotServiceImpl extends ServiceImpl<TimeSlotDao, TimeSlotEntity> implements TimeSlotService {
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        // 创建查询条件
        QueryWrapper<TimeSlotEntity> queryWrapper = new QueryWrapper<>();

        // 创建分页对象
        Page<TimeSlotEntity> page = new Page<>(
                params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
                params.containsKey("size") ? Integer.parseInt(params.get("size").toString()) : 10);

        // 使用page方法进行分页查询
        IPage<TimeSlotEntity> iPage = this.page(page, queryWrapper);

        // 返回分页结果
        return new PageUtils(iPage.getRecords(), (int) iPage.getTotal(), (int) iPage.getSize(),
                (int) iPage.getCurrent());
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, QueryWrapper<TimeSlotEntity> wrapper) {
        // 创建分页对象
        Page<TimeSlotEntity> page = new Page<>(
                params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
                params.containsKey("size") ? Integer.parseInt(params.get("size").toString()) : 10);

        // 使用page方法进行分页查询
        IPage<TimeSlotEntity> iPage = this.page(page, wrapper);

        // 返回分页结果
        return new PageUtils(iPage.getRecords(), (int) iPage.getTotal(), (int) iPage.getSize(),
                (int) iPage.getCurrent());
    }
}