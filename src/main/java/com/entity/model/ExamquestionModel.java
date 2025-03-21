package com.entity.model;

import java.io.Serializable;

/**
 * 试题表
 * 接收传参的实体类
 * （实际开发中配合移动端接口开发手动去掉些没用的字段， 后端一般用entity就够用了）
 * 取自ModelAndView 的model名称
 * 
 * @author
 * @email
 * @date 2021-05-04 17:24:35
 */
public class ExamQuestionModel implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 试卷名称
	 */

	private String papername;

	/**
	 * 试题名称
	 */

	private String questionname;

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
	 * 设置：试卷名称
	 */

	public void setPapername(String papername) {
		this.papername = papername;
	}

	/**
	 * 获取：试卷名称
	 */
	public String getPapername() {
		return papername;
	}

	/**
	 * 设置：试题名称
	 */

	public void setQuestionname(String questionname) {
		this.questionname = questionname;
	}

	/**
	 * 获取：试题名称
	 */
	public String getQuestionname() {
		return questionname;
	}

	/**
	 * 设置：选项，json字符串
	 */

	public void setOptions(String options) {
		this.options = options;
	}

	/**
	 * 获取：选项，json字符串
	 */
	public String getOptions() {
		return options;
	}

	/**
	 * 设置：分值
	 */

	public void setScore(Integer score) {
		this.score = score;
	}

	/**
	 * 获取：分值
	 */
	public Integer getScore() {
		return score;
	}

	/**
	 * 设置：正确答案
	 */

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	/**
	 * 获取：正确答案
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * 设置：答案解析
	 */

	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}

	/**
	 * 获取：答案解析
	 */
	public String getAnalysis() {
		return analysis;
	}

	/**
	 * 设置：试题类型，0：单选题 1：多选题 2：判断题 3：填空题（暂不考虑多项填空）
	 */

	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * 获取：试题类型，0：单选题 1：多选题 2：判断题 3：填空题（暂不考虑多项填空）
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * 设置：试题排序，值越大排越前面
	 */

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	/**
	 * 获取：试题排序，值越大排越前面
	 */
	public Integer getSequence() {
		return sequence;
	}

}
