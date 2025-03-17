package com.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 试题表
 * 数据库通用操作实体类（普通增删改查）
 * 
 * @author
 * @email
 * @date 2021-05-04 17:24:35
 */
@TableName("exam_question")
@Data
public class ExamQuestionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private Integer id;
	/**
	 * 所属试卷id（外键）
	 */
	@TableField("paper_id")
	private Integer paperId;

	/**
	 * 试卷名称
	 */
	@TableField("paper_name")
	private String paperName;

	/**
	 * 试题名称
	 */
	@TableField("question_name")
	private String questionName;

	/**
	 * 选项，json字符串
	 */

	private String options;

	/**
	 * 分值
	 */

	private Integer score;

	/**
	 * 正确答案
	 */

	private String answer;

	/**
	 * 答案解析
	 */

	private String analysis;

	/**
	 * 试题类型，0：单选题 1：多选题 2：判断题 3：填空题（暂不考虑多项填空）
	 */

	private Integer type;

	/**
	 * 试题排序，值越大排越前面
	 */

	private Integer sequence;

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
