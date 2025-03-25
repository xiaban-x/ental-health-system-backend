package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.entity.FeedbackEntity;
import com.utils.PageUtils;
import java.util.Map;

/**
 * 反馈服务接口
 */
public interface FeedbackService extends IService<FeedbackEntity> {
    /**
     * 分页查询
     */
    PageUtils queryPage(Map<String, Object> params);
}