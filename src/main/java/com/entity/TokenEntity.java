package com.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;

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
	private Integer userid;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 表名
	 */
	private String tablename;

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
	private Date expiratedtime;

	/**
	 * 新增时间
	 */
	private Date addtime;

}
