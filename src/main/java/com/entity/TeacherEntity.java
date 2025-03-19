package com.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 教师信息表
 * 数据库通用操作实体类（普通增删改查）
 */
@Data
@NoArgsConstructor
@TableName("teacher")
public class TeacherEntity implements Serializable {
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
     * 工号
     */
    @TableField("employee_id")
    private String employeeId;

    /**
     * 职称
     */
    private String title;

    /**
     * 所属院系
     */
    private String department;

    /**
     * 研究领域
     */
    @TableField("research_field")
    private String researchField;

    /**
     * 办公室位置
     */
    @TableField("office_location")
    private String officeLocation;

    /**
     * 办公时间
     */
    @TableField("office_hours")
    private String officeHours;

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