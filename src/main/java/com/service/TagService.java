package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.entity.TagEntity;
import com.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 标签表 服务接口
 */
public interface TagService extends IService<TagEntity> {

    /**
     * 分页查询
     */
    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * 根据资源ID获取标签列表
     */
    List<TagEntity> getTagsByResourceId(Integer resourceId);
    
    /**
     * 获取所有标签
     */
    List<TagEntity> getAllTags();
    
    /**
     * 根据名称获取或创建标签
     */
    TagEntity getOrCreateTag(String name);
}