package com.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * token表
 */
@TableName("token")
@Data
public class TokenEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 用户id
	 */
	@TableField("user_id")
	private Integer userId;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 表名
	 */
	@TableField("table_name")
	private String tableName;

	/**
	 * 角色
	 */
	private String role;

	/**
	 * token
	 */
	private String token;

	/**
	 * 过期时间
	 */
	@TableField("expired_at")
	private Date expiredAt;

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
