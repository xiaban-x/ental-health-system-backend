package com.entity.vo;

import java.io.Serializable;
import lombok.Data;

/**
 * 教师
 * 手机端接口返回实体辅助类
 * （主要作用去除一些不必要的字段）
 * 
 * @author
 * @email
 * @date 2023-05-04 17:24:35
 */
@Data
public class TeacherVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 工号
     */
    private String employeeId;

    /**
     * 职称
     */
    private String title;

    /**
     * 部门
     */
    private String department;

    /**
     * 研究领域
     */
    private String researchField;
}