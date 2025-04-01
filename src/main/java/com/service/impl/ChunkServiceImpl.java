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

        // 确保必要参数不为空
        Integer chunkNumber = chunkInfo.getChunkNumber();
        Integer chunkSize = chunkInfo.getChunkSize();
        Integer totalSize = chunkInfo.getTotalSize();
        String filename = chunkInfo.getFilename();
        String relativePath = chunkInfo.getRelativePath();

        if (chunkNumber == null || chunkSize == null || totalSize == null || filename == null) {
            result.put("exists", false);
            return result;
        }

        // 从数据库中查询分片是否存在，使用多个字段组合判断，与uploadChunk方法保持一致
        QueryWrapper<ChunkInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chunk_number", chunkNumber)
                .eq("chunk_size", chunkSize)
                .eq("total_size", totalSize)
                .eq("filename", filename);

        // 添加文件类型的检查
        if (chunkInfo.getFileType() != null) {
            queryWrapper.eq("file_type", chunkInfo.getFileType());
        }

        // 处理相对路径，考虑当前月份和上个月份
        if (relativePath != null && !relativePath.isEmpty()) {
            // 获取当前月份和上个月份的路径
            String[] paths = getMonthPaths(relativePath);
            String currentMonthPath = paths[0];
            String lastMonthPath = paths[1];

            // 构建OR条件：当前月份路径 OR 上个月份路径
            queryWrapper.and(wrapper -> wrapper
                    .eq("relative_path", currentMonthPath)
                    .or()
                    .eq("relative_path", lastMonthPath));

            System.out.println("检查分片 - 当前月份路径: " + currentMonthPath + ", 上个月份路径: " + lastMonthPath);
        }

        System.out.println("chunkInfo ===> " + chunkInfo);
        ChunkInfo existingChunk = chunkInfoDao.selectOne(queryWrapper);
        System.out.println("existingChunk ===> " + existingChunk);

        // 检查分片是否存在且未过期（7天内）
        boolean exists = false;
        if (existingChunk != null) {
            Date now = new Date();
            long diffInMillies = Math.abs(now.getTime() - existingChunk.getUpdatedAt().getTime());
            long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);

            exists = diffInDays <= 7;
        }

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

        // 首先检查数据库中是否已存在该分片记录，使用多个字段组合判断
        QueryWrapper<ChunkInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chunk_number", chunkNumber)
                .eq("chunk_size", chunkInfo.getChunkSize())
                .eq("total_size", chunkInfo.getTotalSize())
                .eq("filename", chunkInfo.getFilename());

        if (chunkInfo.getFileType() != null) {
            queryWrapper.eq("file_type", chunkInfo.getFileType());
        }

        // 添加相对路径的检查
        if (chunkInfo.getRelativePath() != null) {
            queryWrapper.eq("relative_path", chunkInfo.getRelativePath());
        }

        ChunkInfo existingChunk = chunkInfoDao.selectOne(queryWrapper);

        // 如果分片已存在且未过期（7天内），则不需要重新上传
        if (existingChunk != null) {
            Date now = new Date();
            long diffInMillies = Math.abs(now.getTime() - existingChunk.getUpdatedAt().getTime());
            long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);

            if (diffInDays <= 7) {
                // 分片存在且未过期，不需要任何操作
                return;
            } else {
                // 分片存在但已过期，更新时间戳
                existingChunk.setUpdatedAt(now);
                chunkInfoDao.updateById(existingChunk);
                return;
            }
        }

        // 分片不存在，需要上传到Minio
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

        // 保存分片信息到数据库
        chunkInfoDao.insert(chunkInfo);
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
        String relativePath = chunkInfo.getRelativePath();

        // 首先检查是否已经存在相同的文件（基于identifier和relativePath）
        // 构建最终文件名的格式，与下面合并时使用的格式一致
        String extension = filename.substring(filename.lastIndexOf("."));
        String finalObjectNamePattern = "_" + identifier + extension;

        // 查询数据库中是否有相同identifier的已完成文件
        QueryWrapper<ChunkInfo> existingFileQuery = new QueryWrapper<>();
        existingFileQuery.eq("identifier", identifier)
                .eq("status", 1) // 1-上传完成
                .orderByDesc("updated_at")
                .last("LIMIT 1");

        // 处理相对路径，考虑当前月份和上个月份
        if (relativePath != null && !relativePath.isEmpty()) {
            // 获取当前月份和上个月份的路径
            String[] paths = getMonthPaths(relativePath);
            String currentMonthPath = paths[0];
            String lastMonthPath = paths[1];

            // 构建OR条件：当前月份路径 OR 上个月份路径
            existingFileQuery.and(wrapper -> wrapper
                    .eq("relative_path", currentMonthPath)
                    .or()
                    .eq("relative_path", lastMonthPath));

            System.out.println("合并分片 - 检查已存在文件 - 当前月份路径: " + currentMonthPath + ", 上个月份路径: " + lastMonthPath);
        }

        ChunkInfo existingFile = chunkInfoDao.selectOne(existingFileQuery);

        // 如果找到已存在的完整文件，直接返回其路径
        if (existingFile != null && existingFile.getChunkPath() != null) {
            // 返回已存在的文件路径
            return existingFile.getChunkPath();
        }

        // 从数据库中查询所有分片记录，先查当前月份
        List<ChunkInfo> chunkList = new ArrayList<>();

        if (relativePath != null && !relativePath.isEmpty()) {
            // 获取当前月份和上个月份的路径
            String[] paths = getMonthPaths(relativePath);
            String currentMonthPath = paths[0];
            String lastMonthPath = paths[1];

            // 先查询当前月份的分片
            QueryWrapper<ChunkInfo> currentMonthQuery = new QueryWrapper<>();
            currentMonthQuery.eq("identifier", identifier)
                    .eq("relative_path", currentMonthPath)
                    .orderByAsc("chunk_number");

            List<ChunkInfo> currentMonthChunks = chunkInfoDao.selectList(currentMonthQuery);
            System.out.println("当前月份分片数量: " + currentMonthChunks.size());

            // 如果当前月份的分片数量等于总分片数，直接使用当前月份的分片
            if (currentMonthChunks.size() == totalChunks) {
                chunkList = currentMonthChunks;
            } else {
                // 查询上个月份的分片
                QueryWrapper<ChunkInfo> lastMonthQuery = new QueryWrapper<>();
                lastMonthQuery.eq("identifier", identifier)
                        .eq("relative_path", lastMonthPath)
                        .orderByAsc("chunk_number");

                List<ChunkInfo> lastMonthChunks = chunkInfoDao.selectList(lastMonthQuery);
                System.out.println("上个月份分片数量: " + lastMonthChunks.size());

                // 如果上个月份的分片数量等于总分片数，使用上个月份的分片
                if (lastMonthChunks.size() == totalChunks) {
                    chunkList = lastMonthChunks;
                } else {
                    // 合并两个月份的分片，按照分片号排序
                    Map<Integer, ChunkInfo> chunkMap = new HashMap<>();

                    // 优先使用当前月份的分片
                    for (ChunkInfo chunk : currentMonthChunks) {
                        chunkMap.put(chunk.getChunkNumber(), chunk);
                    }

                    // 如果当前月份没有某个分片，则使用上个月份的
                    for (ChunkInfo chunk : lastMonthChunks) {
                        if (!chunkMap.containsKey(chunk.getChunkNumber())) {
                            chunkMap.put(chunk.getChunkNumber(), chunk);
                        }
                    }

                    // 将Map转换为List并按分片号排序
                    chunkList = new ArrayList<>(chunkMap.values());
                    chunkList.sort((a, b) -> a.getChunkNumber().compareTo(b.getChunkNumber()));
                }
            }
        } else {
            // 如果没有相对路径，直接查询所有分片
            QueryWrapper<ChunkInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("identifier", identifier)
                    .orderByAsc("chunk_number");

            chunkList = chunkInfoDao.selectList(queryWrapper);
        }

        // 检查所有分片是否都已上传
        if (chunkList.size() != totalChunks) {
            throw new Exception("分片数量不匹配，无法合并。预期:" + totalChunks + "，实际:" + chunkList.size());
        }

        // 构建合并源
        List<ComposeSource> sources = new ArrayList<>();
        for (ChunkInfo chunk : chunkList) {
            // 获取分片在Minio中的路径
            String chunkPath = chunk.getChunkPath();

            // 确保分片路径存在
            if (chunkPath == null || chunkPath.isEmpty()) {
                throw new Exception("分片路径为空: " + chunk.getChunkNumber());
            }

            sources.add(
                    ComposeSource.builder()
                            .bucket(chunkBucketName) // 分片存储在chunks桶中
                            .object(chunkPath)
                            .build());
        }

        // 生成最终文件名，包含时间戳确保唯一性
        String finalObjectName = System.currentTimeMillis() + finalObjectNamePattern;

        // 如果有相对路径，添加到文件名前，使用当前月份的路径
        if (relativePath != null && !relativePath.isEmpty() && !relativePath.equals("/")) {
            // 确保相对路径格式正确（去掉开头的斜杠，确保结尾有斜杠）
            String formattedPath = relativePath.startsWith("/") ? relativePath.substring(1) : relativePath;
            formattedPath = formattedPath.endsWith("/") ? formattedPath : formattedPath + "/";
            finalObjectName = formattedPath + finalObjectName;
        }

        System.out.println("合并分片 - finalObjectName ==>" + finalObjectName + "  相对路径===" + relativePath);
        System.out.println("合并分片 - 源分片数量: " + sources.size() + ", 目标桶: " + bucketName);

        try {
            // 合并分片到media桶
            minioClient.composeObject(
                    ComposeObjectArgs.builder()
                            .bucket(bucketName) // 最终文件存储在media桶中
                            .object(finalObjectName)
                            .sources(sources)
                            .build());

            System.out.println("合并分片 - 合并成功: " + finalObjectName);
        } catch (Exception e) {
            System.err.println("合并分片失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        // 更新数据库中的分片状态为已完成，并记录最终文件路径
        for (ChunkInfo chunk : chunkList) {
            chunk.setStatus(1); // 1-上传完成
            chunk.setUpdatedAt(new Date());
            // 在所有分片记录中保存最终文件路径，确保后续查询能找到
            chunk.setChunkPath(finalObjectName); // 保存最终文件路径
            chunkInfoDao.updateById(chunk);

            // 删除Minio中的临时分片
            try {
                // 只删除临时分片，不删除最终文件
                String chunkPath = getChunkObjectName(identifier, chunk.getChunkNumber());
                minioService.deleteFile(chunkBucketName, chunkPath);
                System.out.println("删除临时分片成功: " + chunkPath);
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

    /**
     * 获取当前月份和上个月份的路径
     * 例如：输入 "2025/04/video"，返回 ["2025/04/video", "2025/03/video"]
     */
    private String[] getMonthPaths(String relativePath) {
        String[] result = new String[2];
        result[0] = relativePath; // 当前月份路径

        if (relativePath != null && !relativePath.isEmpty()) {
            // 假设路径格式为 "yyyy/MM/xxx"
            String[] parts = relativePath.split("/");
            if (parts.length >= 2) {
                try {
                    int year = Integer.parseInt(parts[0]);
                    int month = Integer.parseInt(parts[1]);

                    // 计算上个月的年份和月份
                    if (month == 1) {
                        year--;
                        month = 12;
                    } else {
                        month--;
                    }

                    // 构建上个月的路径
                    StringBuilder lastMonthPath = new StringBuilder();
                    lastMonthPath.append(year).append("/");
                    // 确保月份是两位数
                    if (month < 10) {
                        lastMonthPath.append("0");
                    }
                    lastMonthPath.append(month);

                    // 添加剩余的路径部分
                    for (int i = 2; i < parts.length; i++) {
                        lastMonthPath.append("/").append(parts[i]);
                    }

                    result[1] = lastMonthPath.toString();
                    return result;
                } catch (NumberFormatException e) {
                    // 如果解析年份或月份失败，返回原路径
                    result[1] = relativePath;
                }
            }
        }

        // 如果无法处理，上个月份路径与当前月份路径相同
        result[1] = relativePath;
        return result;
    }
}