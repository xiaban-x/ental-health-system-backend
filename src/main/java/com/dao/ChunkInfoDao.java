package com.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entity.ChunkInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 分片信息Mapper接口
 */
@Mapper
public interface ChunkInfoDao extends BaseMapper<ChunkInfo> {
}