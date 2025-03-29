package com.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

/**
 * 分片信息实体类
 */
@Data
public class ChunkInfo {
    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Integer id;

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
     * 分片在Minio中的路径
     */
    private String chunkPath;

    /**
     * 状态：0-上传中，1-上传完成
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 分片文件（不存入数据库，仅用于传输）
     */
    private transient MultipartFile file;

    // 在setter方法中添加类型转换逻辑
        public void setChunkNumber(Object chunkNumber) {
            if (chunkNumber instanceof String) {
                this.chunkNumber = Integer.parseInt((String) chunkNumber);
            } else if (chunkNumber instanceof Number) {
                this.chunkNumber = ((Number) chunkNumber).intValue();
            } else {
                this.chunkNumber = null;
            }
        }
        
        // 类似地，为其他数值字段添加类型转换逻辑
}