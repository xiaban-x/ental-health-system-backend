package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dao.ResourceTagDao;
import com.entity.ResourceTagEntity;
import com.service.ResourceTagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 资源标签关联表 服务实现类
 */
@Service("resourceTagService")
public class ResourceTagServiceImpl extends ServiceImpl<ResourceTagDao, ResourceTagEntity> implements ResourceTagService {

    @Override
    @Transactional
    public void addTagsToResource(Integer resourceId, List<Integer> tagIds) {
        if (resourceId == null || tagIds == null || tagIds.isEmpty()) {
            return;
        }
        
        List<ResourceTagEntity> entities = new ArrayList<>();
        for (Integer tagId : tagIds) {
            ResourceTagEntity entity = new ResourceTagEntity();
            entity.setResourceId(resourceId);
            entity.setTagId(tagId);
            entities.add(entity);
        }
        
        this.saveBatch(entities);
    }

    @Override
    public void deleteByResourceId(Integer resourceId) {
        baseMapper.deleteByResourceId(resourceId);
    }
}