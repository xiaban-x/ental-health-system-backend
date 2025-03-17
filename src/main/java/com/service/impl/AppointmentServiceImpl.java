package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.entity.AppointmentEntity;
import com.dao.AppointmentDao;
import com.service.AppointmentService;
import org.springframework.stereotype.Service;

/**
 * 预约服务实现类
 */
@Service
public class AppointmentServiceImpl extends ServiceImpl<AppointmentDao, AppointmentEntity>
        implements AppointmentService {
}