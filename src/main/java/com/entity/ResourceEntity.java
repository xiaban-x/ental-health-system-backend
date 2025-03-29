package com.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 资源表
 * 数据库通用操作实体类（普通增删改查）
 */
@Data
@NoArgsConstructor
@TableName("resource")
public class ResourceEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 资源ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 资源标题
     */
    private String title;

    /**
     * 资源简介
     */
    private String description;

    /**
     * 文章内容（仅文章类型有）
     */
    private String content;

    /**
     * 资源URL（视频、文件等）
     */
    private String url;

    /**
     * 封面图片URL
     */
    @TableField("cover_image")
    private String coverImage;

    /**
     * 资源类型：article-文章，video-视频，tool-工具
     */
    private String type;

    /**
     * 视频时长（秒）
     */
    private Integer duration;

    /**
     * 文件大小（字节）
     */
    private Long size;

    /**
     * 文件格式
     */
    private String format;

    /**
     * 作者ID
     */
    @TableField("author_id")
    private Integer authorId;

    /**
     * 作者名称
     */
    @TableField("author_name")
    private String authorName;

    /**
     * 浏览次数
     */
    @TableField("view_count")
    private Integer viewCount;

    /**
     * 点赞次数
     */
    @TableField("like_count")
    private Integer likeCount;

    /**
     * 状态：0-草稿，1-已发布，2-已下架
     */
    private Integer status;

    /**
     * 创建时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private Date createdAt;

    /**
     * 更新时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private Date updatedAt;
}