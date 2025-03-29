package com.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 标签表
 * 数据库通用操作实体类（普通增删改查）
 */
@Data
@NoArgsConstructor
@TableName("tag")
public class TagEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 标签ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 创建时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private Date createdAt;
}