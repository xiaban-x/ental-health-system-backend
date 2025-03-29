package com.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entity.TagEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 标签表 Dao 接口
 */
@Mapper
public interface TagDao extends BaseMapper<TagEntity> {
    
    /**
     * 根据资源ID获取标签列表
     */
    List<TagEntity> getTagsByResourceId(@Param("resourceId") Integer resourceId);
}