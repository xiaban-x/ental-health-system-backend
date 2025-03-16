package com.entity.view;

import com.entity.UserEntity;

import com.baomidou.mybatisplus.annotation.*;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;

/**
 * 用户
 * 后端返回视图实体辅助类
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * 
 * @author
 * @email
 * @date 2021-05-04 17:24:35
 */
@TableName("user")
public class UserView extends UserEntity {
	private static final long serialVersionUID = 1L;

	public UserView() {
	}

	public UserView(UserEntity userEntity) {
		try {
			BeanUtils.copyProperties(this, userEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
