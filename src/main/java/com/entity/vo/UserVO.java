package com.entity.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * 用户
 * 手机端接口返回实体辅助类
 * （主要作用去除一些不必要的字段）
 * 
 * @author
 * @email
 * @date 2021-05-04 17:24:35
 */
@Data
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
	 * 学号
	 */
	private String studentId;

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
}
