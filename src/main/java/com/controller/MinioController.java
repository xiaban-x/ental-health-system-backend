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

import java.util.HashMap;
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

            // 将返回数据统一放在data字段下
            Map<String, Object> data = Map.of(
                    "url", url,
                    "fileName", fileName);
            return R.ok().put("data", data);
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
    public R checkChunk(@RequestBody Map<String, Object> params) {
        try {
            // 打印接收到的参数，便于调试
            System.out.println("接收到的分片信息: " + params);

            // 从请求参数中提取数据
            ChunkInfo chunkInfo = new ChunkInfo();

            if (params.containsKey("chunkNumber")) {
                chunkInfo.setChunkNumber(Integer.parseInt(params.get("chunkNumber").toString()));
            }

            if (params.containsKey("identifier")) {
                chunkInfo.setIdentifier(params.get("identifier").toString());
            }

            if (params.containsKey("filename")) {
                chunkInfo.setFilename(params.get("filename").toString());
            }

            if (params.containsKey("totalChunks")) {
                chunkInfo.setTotalChunks(Integer.parseInt(params.get("totalChunks").toString()));
            }

            // 其他可选参数
            if (params.containsKey("chunkSize")) {
                // 使用Integer.parseInt进行正确的字符串到整数的转换
                chunkInfo.setChunkSize(Integer.parseInt(params.get("chunkSize").toString()));
            }

            if (params.containsKey("totalSize")) {
                // 同样修复totalSize的转换
                chunkInfo.setTotalSize(Integer.parseInt(params.get("totalSize").toString()));
            }

            if (params.containsKey("fileType")) {
                chunkInfo.setFileType(params.get("fileType").toString());
            }

            // 确保必要参数不为空
            if (chunkInfo.getIdentifier() == null || chunkInfo.getChunkNumber() == null) {
                return R.error("分片标识符或分片编号不能为空");
            }

            // 从数据库中查询分片是否存在
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
    public R uploadChunk(
            @RequestParam("file") MultipartFile file,
            @RequestParam("chunkNumber") Integer chunkNumber,
            @RequestParam("chunkSize") Integer chunkSize,
            @RequestParam("totalSize") Integer totalSize,
            @RequestParam("identifier") String identifier,
            @RequestParam("filename") String filename,
            @RequestParam("totalChunks") Integer totalChunks,
            @RequestParam(value = "fileType", required = false) String fileType,
            @RequestParam(value = "relativePath", required = false) String relativePath) {
        try {
            // 构建ChunkInfo对象
            ChunkInfo chunkInfo = new ChunkInfo();
            chunkInfo.setChunkNumber(chunkNumber);
            chunkInfo.setChunkSize(chunkSize);
            chunkInfo.setTotalSize(totalSize);
            chunkInfo.setIdentifier(identifier);
            chunkInfo.setFilename(filename);
            chunkInfo.setTotalChunks(totalChunks);
            chunkInfo.setFileType(fileType);
            chunkInfo.setFile(file);
            chunkInfo.setRelativePath(relativePath);
            // 上传分片并保存记录到数据库
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
            // 打印请求参数，便于调试
            System.out.println("合并分片请求参数: " + chunkInfo);

            // 合并分片，此操作会删除Minio中的临时分片，但保留数据库记录
            String objectName = chunkService.mergeChunks(chunkInfo);

            // 打印合并后的对象名，便于调试
            System.out.println("合并后的对象名: " + objectName);

            String url = minioService.getFileUrl(objectName);
            System.out.println("文件访问URL: " + url);

            // 将返回数据统一放在data字段下
            Map<String, Object> data = new HashMap<>();
            data.put("url", url);
            data.put("fileName", objectName);

            return R.ok().put("data", data);
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