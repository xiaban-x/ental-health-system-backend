package com.entity.view;

import com.entity.ExamQuestionEntity;

import com.baomidou.mybatisplus.annotation.*;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;

/**
 * 试题表
 * 后端返回视图实体辅助类
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * 
 * @author
 * @email
 * @date 2021-05-04 17:24:35
 */
@TableName("exam_question")
public class ExamQuestionView extends ExamQuestionEntity {
	private static final long serialVersionUID = 1L;

	public ExamQuestionView() {
	}

	public ExamQuestionView(ExamQuestionEntity examquestionEntity) {
		try {
			BeanUtils.copyProperties(this, examquestionEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
