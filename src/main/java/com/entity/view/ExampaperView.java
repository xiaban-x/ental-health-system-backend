package com.entity.view;

import com.entity.ExamPaperEntity;

import com.baomidou.mybatisplus.annotation.*;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;

/**
 * 试卷表
 * 后端返回视图实体辅助类
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * 
 * @author
 * @email
 * @date 2021-05-04 17:24:35
 */
@TableName("exam_paper")
public class ExamPaperView extends ExamPaperEntity {
	private static final long serialVersionUID = 1L;

	public ExamPaperView() {
	}

	public ExamPaperView(ExamPaperEntity exampaperEntity) {
		try {
			BeanUtils.copyProperties(this, exampaperEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
