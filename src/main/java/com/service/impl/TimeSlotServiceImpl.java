package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.entity.TimeSlotEntity;
import com.dao.TimeSlotDao;
import com.service.TimeSlotService;
import org.springframework.stereotype.Service;

/**
 * 时间段服务实现类
 */
@Service
public class TimeSlotServiceImpl extends ServiceImpl<TimeSlotDao, TimeSlotEntity> implements TimeSlotService {
}