package com.entity.model;

import com.entity.YonghuEntity;

import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;

/**
 * 用户
 * 接收传参的实体类
 * （实际开发中配合移动端接口开发手动去掉些没用的字段， 后端一般用entity就够用了）
 * 取自ModelAndView 的model名称
 * 
 * @author
 * @email
 * @date 2021-05-04 17:24:35
 */
public class YonghuModel implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 密码
	 */

	private String mima;

	/**
	 * 姓名
	 */

	private String xingming;

	/**
	 * 性别
	 */

	private String xingbie;

	/**
	 * 手机
	 */

	private String shouji;

	/**
	 * 邮箱
	 */

	private String youxiang;

	/**
	 * 照片
	 */

	private String zhaopian;

	/**
	 * 备注
	 */

	private String beizhu;

	/**
	 * 设置：密码
	 */

	public void setMima(String mima) {
		this.mima = mima;
	}

	/**
	 * 获取：密码
	 */
	public String getMima() {
		return mima;
	}

	/**
	 * 设置：姓名
	 */

	public void setXingming(String xingming) {
		this.xingming = xingming;
	}

	/**
	 * 获取：姓名
	 */
	public String getXingming() {
		return xingming;
	}

	/**
	 * 设置：性别
	 */

	public void setXingbie(String xingbie) {
		this.xingbie = xingbie;
	}

	/**
	 * 获取：性别
	 */
	public String getXingbie() {
		return xingbie;
	}

	/**
	 * 设置：手机
	 */

	public void setShouji(String shouji) {
		this.shouji = shouji;
	}

	/**
	 * 获取：手机
	 */
	public String getShouji() {
		return shouji;
	}

	/**
	 * 设置：邮箱
	 */

	public void setYouxiang(String youxiang) {
		this.youxiang = youxiang;
	}

	/**
	 * 获取：邮箱
	 */
	public String getYouxiang() {
		return youxiang;
	}

	/**
	 * 设置：照片
	 */

	public void setZhaopian(String zhaopian) {
		this.zhaopian = zhaopian;
	}

	/**
	 * 获取：照片
	 */
	public String getZhaopian() {
		return zhaopian;
	}

	/**
	 * 设置：备注
	 */

	public void setBeizhu(String beizhu) {
		this.beizhu = beizhu;
	}

	/**
	 * 获取：备注
	 */
	public String getBeizhu() {
		return beizhu;
	}

}
