package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.dao.FeedbackDao;
import com.entity.FeedbackEntity;
import com.service.FeedbackService;

/**
 * 反馈服务实现类
 */
@Service("feedbackService")
public class FeedbackServiceImpl extends ServiceImpl<FeedbackDao, FeedbackEntity> implements FeedbackService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<FeedbackEntity> queryWrapper = new QueryWrapper<>();

        // 添加查询条件
        if (params.get("userId") != null) {
            queryWrapper.eq("user_id", params.get("userId"));
        }

        if (params.get("type") != null) {
            queryWrapper.eq("type", params.get("type"));
        }

        if (params.get("status") != null) {
            queryWrapper.eq("status", params.get("status"));
        }

        // 按创建时间降序排序
        queryWrapper.orderByDesc("created_at");

        IPage<FeedbackEntity> page = this.page(
                new Page<FeedbackEntity>(
                        params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
                        params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10),
                queryWrapper);
        return new PageUtils(page.getRecords(), (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
    }
}