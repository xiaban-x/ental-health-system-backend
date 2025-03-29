package com.controller;

import com.entity.ChunkInfo;
import com.service.ChunkService;
import com.service.MinioService;
import com.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

/**
 * Minio文件上传控制器
 */
@RestController
@RequestMapping("/api/v1/minio")
@Tag(name = "文件存储", description = "基于Minio的文件上传下载接口")
public class MinioController {

    @Autowired
    private MinioService minioService;

    @Autowired
    private ChunkService chunkService;

    /**
     * 普通文件上传
     */
    @Operation(summary = "普通文件上传", description = "上传单个文件到Minio")
    @Parameter(name = "file", description = "要上传的文件", required = true)
    @PostMapping("/upload")
    public R upload(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") +
                    getFileExtension(file.getOriginalFilename());
            String url = minioService.uploadFile(file, fileName);
            return R.ok().put("url", url).put("fileName", fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("上传失败: " + e.getMessage());
        }
    }

    /**
     * 检查分片是否存在
     */
    @Operation(summary = "检查分片是否存在", description = "检查指定分片是否已上传")
    @PostMapping("/chunk/check")
    public R checkChunk(ChunkInfo chunkInfo) {
        try {
            Map<String, Object> result = chunkService.checkChunkExists(chunkInfo);
            return R.ok().put("data", result);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("检查分片失败: " + e.getMessage());
        }
    }

    /**
     * 上传分片
     */
    @Operation(summary = "上传分片", description = "上传单个分片")
    @PostMapping("/chunk/upload")
    public R uploadChunk(ChunkInfo chunkInfo) {
        try {
            chunkService.uploadChunk(chunkInfo);
            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("上传分片失败: " + e.getMessage());
        }
    }

    /**
     * 合并分片
     */
    @Operation(summary = "合并分片", description = "合并所有已上传的分片")
    @PostMapping("/chunk/merge")
    public R mergeChunks(@RequestBody ChunkInfo chunkInfo) {
        try {
            String objectName = chunkService.mergeChunks(chunkInfo);
            String url = minioService.getFileUrl(objectName);
            return R.ok().put("url", url).put("fileName", objectName);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("合并分片失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        if (filename == null)
            return "";
        int lastDotIndex = filename.lastIndexOf(".");
        return lastDotIndex == -1 ? "" : filename.substring(lastDotIndex);
    }
}