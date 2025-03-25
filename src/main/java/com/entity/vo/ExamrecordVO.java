package com.entity.vo;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;

import lombok.Data;

/**
 * 考试记录表
 * 手机端接口返回实体辅助类
 * （主要作用去除一些不必要的字段）
 * 
 * @author
 * @email
 * @date 2021-05-04 17:24:35
 */
@Data
public class ExamRecordVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户名
	 */

	private String username;

	/**
	 * 试卷id（外键）
	 */
	@TableField(value = "paper_id")
	private Integer paperId;

	/**
	 * 试卷名称
	 */
	@TableField(value = "paper_name")
	private String paperName;

	/**
	 * 试题id（外键）
	 */
	@TableField(value = "question_id")
	private Integer questionId;

	/**
	 * 试题名称
	 */
	@TableField(value = "question_name")
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
	 * 试题得分
	 */
	@TableField(value = "total_score")
	private Integer userScore;

	/**
	 * 考生答案
	 */
	@TableField(value = "feedback")
	private String userAnswer;
}
