package com.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;

/**
 * 类说明 :
 */
@TableName("config")
@Data
public class ConfigEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * key
	 */
	private String name;

	/**
	 * value
	 */
	private String value;
}
