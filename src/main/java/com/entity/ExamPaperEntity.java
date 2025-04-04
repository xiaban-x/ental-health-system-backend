package com.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 试卷表
 * 数据库通用操作实体类（普通增删改查）
 */
@Data
@NoArgsConstructor
@TableName("exam_paper")
public class ExamPaperEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 试卷名称
	 */
	private String title;

	/**
	 * 试卷描述
	 */
	private String description;

	/**
	 * 问卷时长(分钟)
	 */
	private Integer time;

	/**
	 * 试卷状态
	 */
	private Integer status;

	/**
	 * 用户Id
	 */
	@TableField("user_id")
	private Integer userId;

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
