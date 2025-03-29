package com.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entity.ResourceTagEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 资源标签关联表 Dao 接口
 */
@Mapper
public interface ResourceTagDao extends BaseMapper<ResourceTagEntity> {
    
    /**
     * 删除资源的所有标签关联
     */
    void deleteByResourceId(@Param("resourceId") Integer resourceId);
}