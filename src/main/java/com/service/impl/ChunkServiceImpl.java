package com.service.impl;

import com.entity.ChunkInfo;
import com.service.ChunkService;
import io.minio.ComposeObjectArgs;
import io.minio.ComposeSource;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分片上传服务实现类
 */
@Service("chunkService")
public class ChunkServiceImpl implements ChunkService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

    @Value("${minio.bucket.chunks}")
    private String chunkBucketName;

    // 存储分片上传的状态
    private static final Map<String, Map<Integer, Boolean>> CHUNK_UPLOAD_STATUS = new ConcurrentHashMap<>();

    /**
     * 检查分片是否已上传
     */
    @Override
    public Map<String, Object> checkChunkExists(ChunkInfo chunkInfo) {
        Map<String, Object> result = new HashMap<>();
        
        // 确保identifier和chunkNumber不为空
        String identifier = chunkInfo.getIdentifier();
        Integer chunkNumber = chunkInfo.getChunkNumber();
        
        if (identifier == null || chunkNumber == null) {
            result.put("exists", false);
            return result;
        }
        
        // 获取该文件的上传状态
        Map<Integer, Boolean> uploadStatus = CHUNK_UPLOAD_STATUS.getOrDefault(identifier, new HashMap<>());
        
        // 检查分片是否已上传
        boolean exists = uploadStatus.getOrDefault(chunkNumber, false);
        
        result.put("exists", exists);
        return result;
    }

    /**
     * 上传分片
     */
    @Override
    public void uploadChunk(ChunkInfo chunkInfo) throws Exception {
        String identifier = chunkInfo.getIdentifier();
        Integer chunkNumber = chunkInfo.getChunkNumber();
        
        // 分片对象名
        String chunkObjectName = getChunkObjectName(identifier, chunkNumber);
        
        // 上传分片到Minio
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(chunkBucketName)
                        .object(chunkObjectName)
                        .stream(chunkInfo.getFile().getInputStream(), chunkInfo.getFile().getSize(), -1)
                        .contentType(chunkInfo.getFile().getContentType())
                        .build());
        
        // 更新上传状态
        Map<Integer, Boolean> uploadStatus = CHUNK_UPLOAD_STATUS.getOrDefault(identifier, new HashMap<>());
        uploadStatus.put(chunkNumber, true);
        CHUNK_UPLOAD_STATUS.put(identifier, uploadStatus);
    }

    /**
     * 合并分片
     */
    @Override
    public String mergeChunks(ChunkInfo chunkInfo) throws Exception {
        String identifier = chunkInfo.getIdentifier();
        Integer totalChunks = chunkInfo.getTotalChunks();
        String filename = chunkInfo.getFilename();
        
        // 检查所有分片是否都已上传
        Map<Integer, Boolean> uploadStatus = CHUNK_UPLOAD_STATUS.getOrDefault(identifier, new HashMap<>());
        for (int i = 1; i <= totalChunks; i++) {
            if (!uploadStatus.getOrDefault(i, false)) {
                throw new Exception("分片 " + i + " 未上传，无法合并");
            }
        }
        
        // 构建合并源
        List<ComposeSource> sources = new ArrayList<>();
        for (int i = 1; i <= totalChunks; i++) {
            String chunkObjectName = getChunkObjectName(identifier, i);
            sources.add(
                    ComposeSource.builder()
                            .bucket(chunkBucketName)
                            .object(chunkObjectName)
                            .build()
            );
        }
        
        // 生成最终文件名
        String extension = filename.substring(filename.lastIndexOf("."));
        String finalObjectName = System.currentTimeMillis() + "_" + identifier + extension;
        
        // 合并分片
        minioClient.composeObject(
                ComposeObjectArgs.builder()
                        .bucket(bucketName)
                        .object(finalObjectName)
                        .sources(sources)
                        .build()
        );
        
        // 清理分片状态
        CHUNK_UPLOAD_STATUS.remove(identifier);
        
        // 返回文件名
        return finalObjectName;
    }

    /**
     * 获取分片对象名
     */
    private String getChunkObjectName(String identifier, Integer chunkNumber) {
        return identifier + "_" + chunkNumber;
    }
}