package com.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 医生信息表
 * 数据库通用操作实体类（普通增删改查）
 */
@Data
@NoArgsConstructor
@TableName("doctor")
public class DoctorEntity implements Serializable {
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
     * 执业证号
     */
    @TableField("license_number")
    private String licenseNumber;

    /**
     * 职称
     */
    private String title;

    /**
     * 级别(主治医师/副主任医师/主任医师)
     */
    private String level;

    /**
     * 专业方向
     */
    private String specialty;

    /**
     * 科室
     */
    private String department;

    /**
     * 从业年限
     */
    @TableField("experience_years")
    private Integer experienceYears;

    /**
     * 资质认证
     */
    private String certification;

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