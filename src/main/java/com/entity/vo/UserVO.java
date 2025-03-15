package com.entity.vo;

import java.io.Serializable;

/**
 * 用户
 * 手机端接口返回实体辅助类
 * （主要作用去除一些不必要的字段）
 * 
 * @author
 * @email
 * @date 2021-05-04 17:24:35
 */
public class UserVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 密码
	 */

	private String password;

	/**
	 * 姓名
	 */

	private String name;

	/**
	 * 性别
	 */

	private String sex;

	/**
	 * 手机
	 */

	private String phone;

	/**
	 * 邮箱
	 */

	private String email;

	/**
	 * 照片
	 */

	private String avatar;

	/**
	 * 备注
	 */

	private String remark;

	/**
	 * 设置：密码
	 */

	public void setMima(String password) {
		this.password = password;
	}

	/**
	 * 获取：密码
	 */
	public String getMima() {
		return password;
	}

	/**
	 * 设置：姓名
	 */

	public void setXingming(String name) {
		this.name = name;
	}

	/**
	 * 获取：姓名
	 */
	public String getXingming() {
		return name;
	}

	/**
	 * 设置：性别
	 */

	public void setXingbie(String sex) {
		this.sex = sex;
	}

	/**
	 * 获取：性别
	 */
	public String getXingbie() {
		return sex;
	}

	/**
	 * 设置：手机
	 */

	public void setShouji(String phone) {
		this.phone = phone;
	}

	/**
	 * 获取：手机
	 */
	public String getShouji() {
		return phone;
	}

	/**
	 * 设置：邮箱
	 */

	public void setYouxiang(String email) {
		this.email = email;
	}

	/**
	 * 获取：邮箱
	 */
	public String getYouxiang() {
		return email;
	}

	/**
	 * 设置：照片
	 */

	public void setZhaopian(String avatar) {
		this.avatar = avatar;
	}

	/**
	 * 获取：照片
	 */
	public String getZhaopian() {
		return avatar;
	}

	/**
	 * 设置：备注
	 */

	public void setBeizhu(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取：备注
	 */
	public String getBeizhu() {
		return remark;
	}

}
