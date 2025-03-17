package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.entity.CounselorEntity;
import com.dao.CounselorDao;
import com.service.CounselorService;
import org.springframework.stereotype.Service;

/**
 * 咨询师服务实现类
 */
@Service
public class CounselorServiceImpl extends ServiceImpl<CounselorDao, CounselorEntity> implements CounselorService {
}