package com.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 分片信息实体类
 */
@Data
public class ChunkInfo {
    /**
     * 当前分片，从1开始
     */
    private Integer chunkNumber;
    
    /**
     * 分片大小
     */
    private Long chunkSize;
    
    /**
     * 当前分片大小
     */
    private Long currentChunkSize;
    
    /**
     * 总大小
     */
    private Long totalSize;
    
    /**
     * 文件标识
     */
    private String identifier;
    
    /**
     * 文件名
     */
    private String filename;
    
    /**
     * 相对路径
     */
    private String relativePath;
    
    /**
     * 总分片数
     */
    private Integer totalChunks;
    
    /**
     * 文件类型
     */
    private String fileType;
    
    /**
     * 分片文件
     */
    private MultipartFile file;
}