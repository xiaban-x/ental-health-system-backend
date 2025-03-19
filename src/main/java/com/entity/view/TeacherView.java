package com.entity.view;

import com.entity.TeacherEntity;

import com.baomidou.mybatisplus.annotation.*;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;

/**
 * 教师
 * 后端返回视图实体辅助类
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * 
 * @author
 * @email
 * @date 2023-05-04 17:24:35
 */
@TableName("teacher")
public class TeacherView extends TeacherEntity {
	private static final long serialVersionUID = 1L;

	public TeacherView() {
	}

	public TeacherView(TeacherEntity teacherEntity) {
		try {
			BeanUtils.copyProperties(this, teacherEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}