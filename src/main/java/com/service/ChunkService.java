package com.service;

import com.entity.ChunkInfo;
import java.util.Map;

/**
 * 分片上传服务接口
 */
public interface ChunkService {
    
    /**
     * 检查分片是否已上传
     * @param chunkInfo 分片信息
     * @return 检查结果
     */
    Map<String, Object> checkChunkExists(ChunkInfo chunkInfo) throws Exception;
    
    /**
     * 上传分片
     * @param chunkInfo 分片信息
     */
    void uploadChunk(ChunkInfo chunkInfo) throws Exception;
    
    /**
     * 合并分片
     * @param chunkInfo 分片信息
     * @return 合并后的文件名
     */
    String mergeChunks(ChunkInfo chunkInfo) throws Exception;
}