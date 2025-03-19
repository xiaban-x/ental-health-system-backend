package com.entity.vo;

import java.io.Serializable;
import lombok.Data;

/**
 * 医生
 * 手机端接口返回实体辅助类
 * （主要作用去除一些不必要的字段）
 * 
 * @author
 * @email
 * @date 2023-05-04 17:24:35
 */
@Data
public class DoctorVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 执照号
     */
    private String licenseNumber;

    /**
     * 职称
     */
    private String title;

    /**
     * 级别
     */
    private String level;

    /**
     * 专长
     */
    private String specialty;

    /**
     * 部门
     */
    private String department;

    /**
     * 从业年限
     */
    private Integer experienceYears;
}