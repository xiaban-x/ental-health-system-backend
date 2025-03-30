package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dao.ChunkInfoDao;
import com.entity.ChunkInfo;
import com.service.ChunkService;
import com.service.MinioService;
import io.minio.ComposeObjectArgs;
import io.minio.ComposeSource;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分片上传服务实现类
 */
@Service("chunkService")
public class ChunkServiceImpl implements ChunkService {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioService minioService;

    @Autowired
    private ChunkInfoDao chunkInfoDao;

    @Value("${minio.bucket.name}")
    private String bucketName;

    @Value("${minio.bucket.chunks}")
    private String chunkBucketName;

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

        // 从数据库中查询分片是否存在
        QueryWrapper<ChunkInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("identifier", identifier)
                .eq("chunk_number", chunkNumber);

        ChunkInfo existingChunk = chunkInfoDao.selectOne(queryWrapper);
        boolean exists = existingChunk != null;

        result.put("exists", exists);
        return result;
    }

    /**
     * 上传分片
     */
    @Override
    @Transactional
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

        // 设置分片在Minio中的路径
        chunkInfo.setChunkPath(chunkObjectName);
        chunkInfo.setStatus(0); // 0-上传中
        chunkInfo.setCreatedAt(new Date());
        chunkInfo.setUpdatedAt(new Date());

        // 检查数据库中是否已存在该分片记录，使用多个字段组合判断
        QueryWrapper<ChunkInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chunk_number", chunkNumber)
                .eq("chunk_size", chunkInfo.getChunkSize())
                .eq("total_size", chunkInfo.getTotalSize())
                .eq("filename", chunkInfo.getFilename());

        if (chunkInfo.getFileType() != null) {
            queryWrapper.eq("file_type", chunkInfo.getFileType());
        }

        ChunkInfo existingChunk = chunkInfoDao.selectOne(queryWrapper);

        if (existingChunk == null) {
            // 保存分片信息到数据库
            chunkInfoDao.insert(chunkInfo);
        } else {
            // 判断已存在记录的更新时间是否超过7天
            Date now = new Date();
            long diffInMillies = Math.abs(now.getTime() - existingChunk.getUpdatedAt().getTime());
            long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);

            if (diffInDays > 7) {
                // 如果超过7天，更新记录的时间戳
                existingChunk.setUpdatedAt(now);
                chunkInfoDao.updateById(existingChunk);
            }
            // 如果没超过7天，不做任何操作，因为分片已存在且是最近上传的
        }
    }

    /**
     * 合并分片
     */
    @Override
    @Transactional
    public String mergeChunks(ChunkInfo chunkInfo) throws Exception {
        String identifier = chunkInfo.getIdentifier();
        Integer totalChunks = chunkInfo.getTotalChunks();
        String filename = chunkInfo.getFilename();

        // 从数据库中查询所有分片记录
        QueryWrapper<ChunkInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("identifier", identifier)
                .orderByAsc("chunk_number");

        List<ChunkInfo> chunkList = chunkInfoDao.selectList(queryWrapper);

        // 检查所有分片是否都已上传
        if (chunkList.size() != totalChunks) {
            throw new Exception("分片数量不匹配，无法合并");
        }

        // 构建合并源
        List<ComposeSource> sources = new ArrayList<>();
        for (int i = 1; i <= totalChunks; i++) {
            String chunkObjectName = getChunkObjectName(identifier, i);
            sources.add(
                    ComposeSource.builder()
                            .bucket(chunkBucketName)
                            .object(chunkObjectName)
                            .build());
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
                        .build());

        // 更新数据库中的分片状态为已完成
        for (ChunkInfo chunk : chunkList) {
            chunk.setStatus(1); // 1-上传完成
            chunk.setUpdatedAt(new Date());
            chunkInfoDao.updateById(chunk);

            // 删除Minio中的临时分片
            try {
                minioService.deleteFile(chunk.getChunkPath());
            } catch (Exception e) {
                // 记录错误但不中断流程
                System.err.println("删除临时分片失败: " + e.getMessage());
            }
        }

        // 返回文件名
        return finalObjectName;
    }

    /**
     * 获取分片对象名
     */
    private String getChunkObjectName(String identifier, Integer chunkNumber) {
        return "chunks/" + identifier + "/" + chunkNumber;
    }
}