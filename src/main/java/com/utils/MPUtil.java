package com.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import cn.hutool.core.bean.BeanUtil;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * Mybatis-Plus工具类
 */
public class MPUtil {
	public static final char UNDERLINE = '_';

	// mybatis plus allEQ 表达式转换
	public static Map<String, Object> allEQMapPre(Object bean, String pre) {
		Map<String, Object> map = BeanUtil.beanToMap(bean);
		return camelToUnderlineMap(map, pre);
	}

	// mybatis plus allEQ 表达式转换
	public static Map<String, Object> allEQMap(Object bean) {
		Map<String, Object> map = BeanUtil.beanToMap(bean);
		return camelToUnderlineMap(map, "");
	}

	public static QueryWrapper<?> allLikePre(QueryWrapper<?> wrapper, Object bean, String pre) {
		Map<String, Object> map = BeanUtil.beanToMap(bean);
		Map<String, Object> result = camelToUnderlineMap(map, pre);
		return genLike(wrapper, result);
	}

	public static QueryWrapper<?> allLike(QueryWrapper<?> wrapper, Object bean) {
		Map<String, Object> result = BeanUtil.beanToMap(bean, true, true);
		return genLike(wrapper, result);
	}

	public static QueryWrapper<?> genLike(QueryWrapper<?> wrapper, Map<String, Object> param) {
		Iterator<Map.Entry<String, Object>> it = param.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			String value = (String) entry.getValue();
			// 移除 and() 调用，使用链式调用
			wrapper.like(key, value);
		}
		return wrapper;
	}

	public static QueryWrapper<?> likeOrEq(QueryWrapper<?> wrapper, Object bean) {
		Map<String, Object> result = BeanUtil.beanToMap(bean, true, true);
		return genLikeOrEq(wrapper, result);
	}

	public static QueryWrapper<?> genLikeOrEq(QueryWrapper<?> wrapper, Map<String, Object> param) {
		Iterator<Map.Entry<String, Object>> it = param.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			// 移除 and() 调用，使用链式调用
			if (entry.getValue().toString().contains("%")) {
				wrapper.like(key, entry.getValue().toString().replace("%", ""));
			} else {
				wrapper.eq(key, entry.getValue());
			}
		}
		return wrapper;
	}

	public static QueryWrapper<?> allEq(QueryWrapper<?> wrapper, Object bean) {
		Map<String, Object> result = BeanUtil.beanToMap(bean, true, true);
		return genEq(wrapper, result);
	}

	public static QueryWrapper<?> genEq(QueryWrapper<?> wrapper, Map<String, Object> param) {
		Iterator<Map.Entry<String, Object>> it = param.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			// 移除 and() 调用，使用链式调用
			wrapper.eq(entry.getKey(), entry.getValue());
		}
		return wrapper;
	}

	public static QueryWrapper<?> between(QueryWrapper<?> wrapper, Map<String, Object> params) {
		for (String key : params.keySet()) {
			String columnName = "";
			if (key.endsWith("_start")) {
				columnName = key.substring(0, key.indexOf("_start"));
				if (StringUtils.isNotBlank(params.get(key).toString())) {
					wrapper.ge(columnName, params.get(key));
				}
			}
			if (key.endsWith("_end")) {
				columnName = key.substring(0, key.indexOf("_end"));
				if (StringUtils.isNotBlank(params.get(key).toString())) {
					wrapper.le(columnName, params.get(key));
				}
			}
		}
		return wrapper;
	}

	public static QueryWrapper<?> sort(QueryWrapper<?> wrapper, Map<String, Object> params) {
		String order = "";
		if (params.get("order") != null && StringUtils.isNotBlank(params.get("order").toString())) {
			order = params.get("order").toString();
		}
		if (params.get("sort") != null && StringUtils.isNotBlank(params.get("sort").toString())) {
			if (order.equalsIgnoreCase("desc")) {
				wrapper.orderByDesc(params.get("sort").toString());
			} else {
				wrapper.orderByAsc(params.get("sort").toString());
			}
		}
		return wrapper;
	}

	/**
	 * 驼峰格式字符串转换为下划线格式字符串
	 * 
	 * @param param
	 * @return
	 */
	public static String camelToUnderline(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append(UNDERLINE);
				sb.append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static Map<String, Object> camelToUnderlineMap(Map<String, Object> param, String pre) {
		Map<String, Object> newMap = new HashMap<>();
		Iterator<Map.Entry<String, Object>> it = param.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			String newKey = camelToUnderline(key);
			if (pre.endsWith(".")) {
				newMap.put(pre + newKey, entry.getValue());
			} else if (StringUtils.isEmpty(pre)) {
				newMap.put(newKey, entry.getValue());
			} else {
				newMap.put(pre + "." + newKey, entry.getValue());
			}
		}
		return newMap;
	}
}
