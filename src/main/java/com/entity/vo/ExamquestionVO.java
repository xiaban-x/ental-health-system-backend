package com.entity.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 试题表
 * 手机端接口返回实体辅助类
 * （主要作用去除一些不必要的字段）
 * 
 * @author
 * @email
 * @date 2021-05-04 17:24:35
 */
@Data
public class ExamQuestionVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 试卷名称
	 */

	private String paperName;

	/**
	 * 试题名称
	 */

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

}
