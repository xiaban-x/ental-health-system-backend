package com.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.entity.ResourceEntity;
import com.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 资源表 服务接口
 */
public interface ResourceService extends IService<ResourceEntity> {

    /**
     * 分页查询
     */
    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * 条件分页查询
     */
    PageUtils queryPage(Map<String, Object> params, QueryWrapper<ResourceEntity> wrapper);
    
    /**
     * 查询视图列表
     */
    List<ResourceEntity> selectListView(QueryWrapper<ResourceEntity> wrapper);
    
    /**
     * 根据ID获取资源详情（包含标签信息）
     */
    Map<String, Object> getResourceDetail(Integer id);
    
    /**
     * 保存资源及其标签
     */
    void saveResourceWithTags(ResourceEntity resource, List<Integer> tagIds);
    
    /**
     * 更新资源及其标签
     */
    void updateResourceWithTags(ResourceEntity resource, List<Integer> tagIds);
    
    /**
     * 增加浏览次数
     */
    void incrementViewCount(Integer id);
    
    /**
     * 增加点赞次数
     */
    void incrementLikeCount(Integer id);
}