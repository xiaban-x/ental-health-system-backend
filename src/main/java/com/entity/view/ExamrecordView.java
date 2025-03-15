package com.entity.view;

import com.entity.ExamRecordEntity;

import com.baomidou.mybatisplus.annotation.*;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;

/**
 * 考试记录表
 * 后端返回视图实体辅助类
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * 
 * @author
 * @email
 * @date 2021-05-04 17:24:35
 */
@TableName("examrecord")
public class ExamRecordView extends ExamRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public ExamRecordView() {
	}

	public ExamRecordView(ExamRecordEntity examrecordEntity) {
		try {
			BeanUtils.copyProperties(this, examrecordEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
