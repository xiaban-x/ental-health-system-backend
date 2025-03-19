package com.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学生信息表
 * 数据库通用操作实体类（普通增删改查）
 */
@Data
@NoArgsConstructor
@TableName("student")
public class StudentEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 学号
     */
    @TableField("student_id")
    private String studentId;

    /**
     * 专业
     */
    private String major;

    /**
     * 班级
     */
    @TableField("class_name")
    private String className;

    /**
     * 年级
     */
    private String grade;

    /**
     * 入学日期
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("enrollment_date")
    private Date enrollmentDate;

    /**
     * 预计毕业日期
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("graduation_date")
    private Date graduationDate;

    /**
     * 宿舍信息
     */
    private String dormitory;

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