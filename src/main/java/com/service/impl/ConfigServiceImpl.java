
package com.service.impl;

import java.util.Map;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dao.ConfigDao;
import com.entity.ConfigEntity;
import com.service.ConfigService;
import com.utils.PageUtils;

@Service("configService")
public class ConfigServiceImpl extends ServiceImpl<ConfigDao, ConfigEntity> implements ConfigService {
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ConfigEntity> page = this.page(
                new Page<ConfigEntity>(
                        params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
                        params.containsKey("limit") ? Integer.parseInt(params.get("limit").toString()) : 10),
                new QueryWrapper<ConfigEntity>());
        return new PageUtils(page.getRecords(), (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
    }
}
