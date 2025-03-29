package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dao.ResourceDao;
import com.entity.ResourceEntity;
import com.entity.TagEntity;
import com.service.ResourceService;
import com.service.ResourceTagService;
import com.service.TagService;
import com.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资源表 服务实现类
 */
@Service("resourceService")
public class ResourceServiceImpl extends ServiceImpl<ResourceDao, ResourceEntity> implements ResourceService {

    @Autowired
    private TagService tagService;
    
    @Autowired
    private ResourceTagService resourceTagService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        // 创建查询条件
        QueryWrapper<ResourceEntity> queryWrapper = new QueryWrapper<>();
        
        // 创建分页对象
        Page<ResourceEntity> page = new Page<>(
                params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
                params.containsKey("size") ? Integer.parseInt(params.get("size").toString()) : 10);
        
        // 使用page方法进行分页查询
        IPage<ResourceEntity> iPage = this.page(page, queryWrapper);
        
        // 返回分页结果
        return new PageUtils(iPage.getRecords(), (int) iPage.getTotal(), (int) iPage.getSize(), (int) iPage.getCurrent());
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, QueryWrapper<ResourceEntity> wrapper) {
        // 创建分页对象
        Page<ResourceEntity> page = new Page<>(
                params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
                params.containsKey("size") ? Integer.parseInt(params.get("size").toString()) : 10);
        
        // 使用page方法进行分页查询
        IPage<ResourceEntity> iPage = this.page(page, wrapper);
        
        // 返回分页结果
        return new PageUtils(iPage.getRecords(), (int) iPage.getTotal(), (int) iPage.getSize(), (int) iPage.getCurrent());
    }

    @Override
    public List<ResourceEntity> selectListView(QueryWrapper<ResourceEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public Map<String, Object> getResourceDetail(Integer id) {
        // 获取资源信息
        ResourceEntity resource = this.getById(id);
        if (resource == null) {
            return null;
        }
        
        // 获取资源标签
        List<TagEntity> tags = tagService.getTagsByResourceId(id);
        
        // 组装结果
        Map<String, Object> result = new HashMap<>();
        result.put("resource", resource);
        result.put("tags", tags);
        
        return result;
    }

    @Override
    @Transactional
    public void saveResourceWithTags(ResourceEntity resource, List<Integer> tagIds) {
        // 保存资源
        this.save(resource);
        
        // 保存资源标签关联
        if (tagIds != null && !tagIds.isEmpty()) {
            resourceTagService.addTagsToResource(resource.getId(), tagIds);
        }
    }

    @Override
    @Transactional
    public void updateResourceWithTags(ResourceEntity resource, List<Integer> tagIds) {
        // 更新资源
        this.updateById(resource);
        
        // 删除原有标签关联
        resourceTagService.deleteByResourceId(resource.getId());
        
        // 保存新的标签关联
        if (tagIds != null && !tagIds.isEmpty()) {
            resourceTagService.addTagsToResource(resource.getId(), tagIds);
        }
    }

    @Override
    public void incrementViewCount(Integer id) {
        ResourceEntity resource = this.getById(id);
        if (resource != null) {
            resource.setViewCount(resource.getViewCount() + 1);
            this.updateById(resource);
        }
    }

    @Override
    public void incrementLikeCount(Integer id) {
        ResourceEntity resource = this.getById(id);
        if (resource != null) {
            resource.setLikeCount(resource.getLikeCount() + 1);
            this.updateById(resource);
        }
    }
}