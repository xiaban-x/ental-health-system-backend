package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.entity.ResourceTagEntity;

import java.util.List;

/**
 * 资源标签关联表 服务接口
 */
public interface ResourceTagService extends IService<ResourceTagEntity> {

    /**
     * 为资源添加标签
     */
    void addTagsToResource(Integer resourceId, List<Integer> tagIds);
    
    /**
     * 删除资源的所有标签关联
     */
    void deleteByResourceId(Integer resourceId);
}