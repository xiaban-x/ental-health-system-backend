package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.entity.AppointmentEntity;
import com.dao.AppointmentDao;
import com.service.AppointmentService;
import com.utils.PageUtils;

import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * 预约服务实现类
 */
@Service
public class AppointmentServiceImpl extends ServiceImpl<AppointmentDao, AppointmentEntity>
                implements AppointmentService {
        @Override
        public PageUtils queryPage(Map<String, Object> params) {
                IPage<AppointmentEntity> page = this.page(
                                new Page<AppointmentEntity>(
                                                params.containsKey("page")
                                                                ? Integer.parseInt(params.get("page").toString())
                                                                : 1,
                                                params.containsKey("limit")
                                                                ? Integer.parseInt(params.get("limit").toString())
                                                                : 10),
                                new QueryWrapper<AppointmentEntity>());
                return new PageUtils(page.getRecords(), (int) page.getTotal(), (int) page.getSize(),
                                (int) page.getCurrent());
        }

        @Override
        public PageUtils queryPage(Map<String, Object> params, QueryWrapper<AppointmentEntity> wrapper) {
                // 创建分页对象
                Page<AppointmentEntity> page = new Page<>(
                                params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
                                params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10);

                // 使用page方法进行分页查询，这样会在SQL中添加LIMIT子句
                IPage<AppointmentEntity> iPage = this.page(page, wrapper);

                // 返回分页结果
                return new PageUtils(iPage.getRecords(), (int) iPage.getTotal(), (int) iPage.getSize(),
                                (int) iPage.getCurrent());
        }
}