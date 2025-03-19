package com.entity.vo;

import java.io.Serializable;
import lombok.Data;

/**
 * 学生
 * 手机端接口返回实体辅助类
 * （主要作用去除一些不必要的字段）
 * 
 * @author
 * @email
 * @date 2023-05-04 17:24:35
 */
@Data
public class StudentVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 学号
     */
    private String studentId;

    /**
     * 专业
     */
    private String major;

    /**
     * 班级
     */
    private String className;

    /**
     * 年级
     */
    private String grade;
}