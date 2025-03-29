package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dao.TagDao;
import com.entity.TagEntity;
import com.service.TagService;
import com.utils.PageUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 标签表 服务实现类
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagDao, TagEntity> implements TagService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        // 创建分页对象
        Page<TagEntity> page = new Page<>(
                params.containsKey("page") ? Integer.parseInt(params.get("page").toString()) : 1,
                params.containsKey("size") ? Integer.parseInt(params.get("size").toString()) : 10);
        
        // 使用page方法进行分页查询
        IPage<TagEntity> iPage = this.page(page, new QueryWrapper<>());
        
        // 返回分页结果
        return new PageUtils(iPage.getRecords(), (int) iPage.getTotal(), (int) iPage.getSize(), (int) iPage.getCurrent());
    }

    @Override
    public List<TagEntity> getTagsByResourceId(Integer resourceId) {
        return baseMapper.getTagsByResourceId(resourceId);
    }

    @Override
    public List<TagEntity> getAllTags() {
        return this.list();
    }

    @Override
    public TagEntity getOrCreateTag(String name) {
        // 查询是否已存在同名标签
        QueryWrapper<TagEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        TagEntity tag = this.getOne(queryWrapper);
        
        // 如果不存在则创建
        if (tag == null) {
            tag = new TagEntity();
            tag.setName(name);
            this.save(tag);
        }
        
        return tag;
    }
}