package com.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.annotation.IgnoreAuth;
import com.baidu.aip.face.AipFace;
import com.baidu.aip.face.MatchRequest;
import com.baidu.aip.util.Base64Util;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.ConfigEntity;
import com.service.CommonService;
import com.service.ConfigService;
import com.utils.BaiduUtil;
import com.utils.FileUtil;
import com.utils.R;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 通用接口
 * 
 * @author Trae
 * @version 1.0
 */
@RestController
@Tag(name = "通用接口", description = "包括位置查询、人脸比对、数据统计等通用功能")
public class CommonController {
	@Autowired
	private CommonService commonService;

	@Autowired
	private ConfigService configService;

	private static AipFace client = null;

	private static String BAIDU_DITU_AK = null;

	@Operation(summary = "获取地理位置信息", description = "根据经纬度获取位置信息")
	@Parameters({
			@Parameter(name = "lng", description = "经度", required = true),
			@Parameter(name = "lat", description = "纬度", required = true)
	})
	@RequestMapping("/location")
	public R location(String lng, String lat) {
		if (BAIDU_DITU_AK == null) {
			BAIDU_DITU_AK = configService.getOne(new QueryWrapper<ConfigEntity>().eq("name", "baidu_ditu_ak"))
					.getValue();
			if (BAIDU_DITU_AK == null) {
				return R.error("请在配置管理中正确配置baidu_ditu_ak");
			}
		}
		Map<String, String> map = BaiduUtil.getCityByLonLat(BAIDU_DITU_AK, lng, lat);
		return R.ok().put("data", map);
	}

	/**
	 * 人脸比对
	 * 
	 * @param face1 人脸1
	 * @param face2 人脸2
	 * @return
	 */
	@Operation(summary = "人脸比对", description = "比对两张人脸图片的相似度")
	@Parameters({
			@Parameter(name = "face1", description = "第一张人脸图片", required = true),
			@Parameter(name = "face2", description = "第二张人脸图片", required = true)
	})
	@RequestMapping("/matchFace")
	public R matchFace(String face1, String face2) {
		if (client == null) {
			/*
			 * String AppID = configService.selectOne(new
			 * EntityWrapper<ConfigEntity>().eq("name", "AppID")).getValue();
			 */
			String apiKey = null;
			String secretKey = null;
			ConfigEntity apiKeyConfig = configService.getOne(new QueryWrapper<ConfigEntity>().eq("name", "APIKey"));
			ConfigEntity secretKeyConfig = configService
					.getOne(new QueryWrapper<ConfigEntity>().eq("name", "SecretKey"));
			if (apiKeyConfig != null) {
				apiKey = apiKeyConfig.getValue();
			}
			if (secretKeyConfig != null) {
				secretKey = secretKeyConfig.getValue();
			}

			if (StringUtils.isBlank(apiKey) || StringUtils.isBlank(secretKey)) {
				return R.error("请在配置管理中正确配置 APIKey 和 SecretKey");
			}
			String token = BaiduUtil.getAuth(apiKey, secretKey);
			if (token == null) {
				return R.error("请在配置管理中正确配置APIKey和SecretKey");
			}
			client = new AipFace(null, apiKey, secretKey);
			client.setConnectionTimeoutInMillis(2000);
			client.setSocketTimeoutInMillis(60000);
		}
		JSONObject res = null;
		try {
			File file1 = new File(ResourceUtils.getFile("classpath:static/upload").getAbsolutePath() + "/" + face1);
			File file2 = new File(ResourceUtils.getFile("classpath:static/upload").getAbsolutePath() + "/" + face2);
			String img1 = Base64Util.encode(FileUtil.FileToByte(file1));
			String img2 = Base64Util.encode(FileUtil.FileToByte(file2));
			MatchRequest req1 = new MatchRequest(img1, "BASE64");
			MatchRequest req2 = new MatchRequest(img2, "BASE64");
			ArrayList<MatchRequest> requests = new ArrayList<MatchRequest>();
			requests.add(req1);
			requests.add(req2);
			res = client.match(requests);
			System.out.println(res.get("result"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return R.error("文件不存在");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return R.ok().put("data", com.alibaba.fastjson.JSONObject.parse(res.get("result").toString()));
	}

	/**
	 * 获取table表中的column列表(联动接口)
	 * 
	 * @param table
	 * @param column
	 * @return
	 */
	@Operation(summary = "获取选项列表", description = "获取指定表的指定列的所有可选值")
	@Parameters({
			@Parameter(name = "tableName", description = "表名", required = true),
			@Parameter(name = "columnName", description = "列名", required = true),
			@Parameter(name = "level", description = "层级"),
			@Parameter(name = "parent", description = "父级")
	})
	@IgnoreAuth
	@RequestMapping("/option/{tableName}/{columnName}")
	public R getOption(@PathVariable("tableName") String tableName,
			@PathVariable("columnName") String columnName,
			String level, String parent) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("table", tableName);
		params.put("column", columnName);
		if (StringUtils.isNotBlank(level)) {
			params.put("level", level);
		}
		if (StringUtils.isNotBlank(parent)) {
			params.put("parent", parent);
		}
		List<String> data = commonService.getOption(params);
		return R.ok().put("data", data);
	}

	/**
	 * 根据table中的column获取单条记录
	 * 
	 * @param table
	 * @param column
	 * @return
	 */
	@Operation(summary = "获取关联记录", description = "根据表中的列值获取单条记录")
	@Parameters({
			@Parameter(name = "tableName", description = "表名", required = true),
			@Parameter(name = "columnName", description = "列名", required = true),
			@Parameter(name = "columnValue", description = "列值", required = true)
	})
	@IgnoreAuth
	@RequestMapping("/follow/{tableName}/{columnName}")
	public R getFollowByOption(@PathVariable("tableName") String tableName,
			@PathVariable("columnName") String columnName,
			@RequestParam String columnValue) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("table", tableName);
		params.put("column", columnName);
		params.put("columnValue", columnValue);
		Map<String, Object> result = commonService.getFollowByOption(params);
		return R.ok().put("data", result);
	}

	/**
	 * 修改审核状态
	 */
	@Operation(summary = "修改审核状态", description = "修改指定表的审核状态")
	@Parameters({
			@Parameter(name = "tableName", description = "表名", required = true),
			@Parameter(name = "map", description = "包含审核状态的参数", required = true)
	})
	@RequestMapping("/sh/{tableName}")
	public R sh(@PathVariable("tableName") String tableName, @RequestBody Map<String, Object> map) {
		map.put("table", tableName);
		commonService.sh(map);
		return R.ok();
	}

	/**
	 * 获取需要提醒的记录数
	 */
	@Operation(summary = "获取提醒记录数", description = "获取需要提醒的记录数量，支持数字和日期类型")
	@Parameters({
			@Parameter(name = "tableName", description = "表名", required = true),
			@Parameter(name = "columnName", description = "列名", required = true),
			@Parameter(name = "type", description = "类型(1:数字 2:日期)", required = true),
			@Parameter(name = "remindstart", description = "开始提醒天数"),
			@Parameter(name = "remindend", description = "结束提醒天数")
	})
	@IgnoreAuth
	@RequestMapping("/remind/{tableName}/{columnName}/{type}")
	public R remindCount(@PathVariable("tableName") String tableName,
			@PathVariable("columnName") String columnName,
			@PathVariable("type") String type,
			@RequestParam Map<String, Object> map) {
		map.put("table", tableName);
		map.put("column", columnName);
		map.put("type", type);

		if (type.equals("2")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			Date remindStartDate = null;
			Date remindEndDate = null;
			if (map.get("remindstart") != null) {
				Integer remindStart = Integer.parseInt(map.get("remindstart").toString());
				c.setTime(new Date());
				c.add(Calendar.DAY_OF_MONTH, remindStart);
				remindStartDate = c.getTime();
				map.put("remindstart", sdf.format(remindStartDate));
			}
			if (map.get("remindend") != null) {
				Integer remindEnd = Integer.parseInt(map.get("remindend").toString());
				c.setTime(new Date());
				c.add(Calendar.DAY_OF_MONTH, remindEnd);
				remindEndDate = c.getTime();
				map.put("remindend", sdf.format(remindEndDate));
			}
		}

		int count = commonService.remindCount(map);
		return R.ok().put("count", count);
	}

	/**
	 * 单列求和
	 */
	@Operation(summary = "单列求和", description = "计算指定表指定列的总和")
	@Parameters({
			@Parameter(name = "tableName", description = "表名", required = true),
			@Parameter(name = "columnName", description = "要求和的列名", required = true)
	})
	@IgnoreAuth
	@RequestMapping("/cal/{tableName}/{columnName}")
	public R cal(@PathVariable("tableName") String tableName,
			@PathVariable("columnName") String columnName) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("table", tableName);
		params.put("column", columnName);
		Map<String, Object> result = commonService.selectCal(params);
		return R.ok().put("data", result);
	}

	/**
	 * 分组统计
	 */
	@Operation(summary = "分组统计", description = "按指定列进行分组统计，支持日期格式化")
	@Parameters({
			@Parameter(name = "tableName", description = "表名", required = true),
			@Parameter(name = "columnName", description = "分组的列名", required = true)
	})
	@IgnoreAuth
	@RequestMapping("/group/{tableName}/{columnName}")
	public R group(@PathVariable("tableName") String tableName,
			@PathVariable("columnName") String columnName) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("table", tableName);
		params.put("column", columnName);
		List<Map<String, Object>> result = commonService.selectGroup(params);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (Map<String, Object> m : result) {
			for (String k : m.keySet()) {
				if (m.get(k) instanceof Date) {
					m.put(k, sdf.format((Date) m.get(k)));
				}
			}
		}
		return R.ok().put("data", result);
	}

	/**
	 * 按值统计
	 */
	@Operation(summary = "按值统计", description = "按X轴和Y轴进行值统计分析，支持日期格式化")
	@Parameters({
			@Parameter(name = "tableName", description = "表名", required = true),
			@Parameter(name = "xColumnName", description = "X轴列名", required = true),
			@Parameter(name = "yColumnName", description = "Y轴列名", required = true)
	})
	@IgnoreAuth
	@RequestMapping("/value/{tableName}/{xColumnName}/{yColumnName}")
	public R value(@PathVariable("tableName") String tableName,
			@PathVariable("yColumnName") String yColumnName,
			@PathVariable("xColumnName") String xColumnName) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("table", tableName);
		params.put("xColumn", xColumnName);
		params.put("yColumn", yColumnName);
		List<Map<String, Object>> result = commonService.selectValue(params);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (Map<String, Object> m : result) {
			for (String k : m.keySet()) {
				if (m.get(k) instanceof Date) {
					m.put(k, sdf.format((Date) m.get(k)));
				}
			}
		}
		return R.ok().put("data", result);
	}

}
