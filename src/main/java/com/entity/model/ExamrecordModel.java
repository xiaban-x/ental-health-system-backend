package com.entity.model;

import java.io.Serializable;

/**
 * 考试记录表
 * 接收传参的实体类
 * （实际开发中配合移动端接口开发手动去掉些没用的字段， 后端一般用entity就够用了）
 * 取自ModelAndView 的model名称
 * 
 * @author
 * @email
 * @date 2021-05-04 17:24:35
 */
public class ExamRecordModel implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户名
	 */

	private String username;

	/**
	 * 试卷id（外键）
	 */

	private Integer paper_id;

	/**
	 * 试卷名称
	 */

	private String paper_name;

	/**
	 * 试题id（外键）
	 */

	private Integer question_id;

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
	 * 试题得分
	 */

	private Integer total_score;

	/**
	 * 考生答案
	 */

	private String feedback;

	/**
	 * 设置：用户名
	 */

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 获取：用户名
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 设置：试卷id（外键）
	 */

	public void setPaperid(Integer paper_id) {
		this.paper_id = paper_id;
	}

	/**
	 * 获取：试卷id（外键）
	 */
	public Integer getPaperid() {
		return paper_id;
	}

	/**
	 * 设置：试卷名称
	 */

	public void setPapername(String paper_name) {
		this.paper_name = paper_name;
	}

	/**
	 * 获取：试卷名称
	 */
	public String getPapername() {
		return paper_name;
	}

	/**
	 * 设置：试题id（外键）
	 */

	public void setQuestionid(Integer question_id) {
		this.question_id = question_id;
	}

	/**
	 * 获取：试题id（外键）
	 */
	public Integer getQuestionid() {
		return question_id;
	}

	/**
	 * 设置：试题名称
	 */

	public void setQuestionname(String questionName) {
		this.questionName = questionName;
	}

	/**
	 * 获取：试题名称
	 */
	public String getQuestionname() {
		return questionName;
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
	 * 设置：试题得分
	 */

	public void setMyscore(Integer total_score) {
		this.total_score = total_score;
	}

	/**
	 * 获取：试题得分
	 */
	public Integer getMyscore() {
		return total_score;
	}

	/**
	 * 设置：考生答案
	 */

	public void setMyanswer(String feedback) {
		this.feedback = feedback;
	}

	/**
	 * 获取：考生答案
	 */
	public String getMyanswer() {
		return feedback;
	}

}
