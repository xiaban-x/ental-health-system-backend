package com.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.entity.ResourceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资源表 Dao 接口
 */
@Mapper
public interface ResourceDao extends BaseMapper<ResourceEntity> {

    /**
     * 查询视图列表
     */
    List<ResourceEntity> selectListView(@Param("ew") QueryWrapper<ResourceEntity> wrapper);

    /**
     * 分页查询视图列表
     */
    IPage<ResourceEntity> selectListView(Page page, @Param("ew") QueryWrapper<ResourceEntity> wrapper);

    /**
     * 根据ID查询资源详情（包含标签信息）
     */
    ResourceEntity selectResourceDetail(Integer id);
}