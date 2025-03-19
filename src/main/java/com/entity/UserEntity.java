package com.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 用户基础信息
 * 数据库通用操作实体类（普通增删改查）
 */
@TableName("public.user")
@Data
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Integer id;

	/**
	 * 账号
	 */
	private String username;

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
	 * 角色 (student, teacher, doctor)
	 */
	private String role;

	/**
	 * 备注
	 */
	private String remark;

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
