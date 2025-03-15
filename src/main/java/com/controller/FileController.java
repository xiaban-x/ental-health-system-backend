package com.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.entity.ConfigEntity;
import com.entity.EIException;
import com.service.ConfigService;
import com.utils.R;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * 文件上传下载接口
 * 
 * @author Trae
 * @version 1.0
 */
@Tag(name = "文件管理", description = "文件上传下载相关接口")
@RestController
@RequestMapping("file")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class FileController {
	@Autowired
	private ConfigService configService;

	/**
	 * 文件上传接口
	 */
	@Operation(summary = "文件上传", description = "上传文件并返回文件名，支持人脸识别文件的特殊处理")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "上传成功"),
			@ApiResponse(responseCode = "500", description = "上传失败")
	})
	@Parameters({
			@Parameter(name = "file", description = "要上传的文件", required = true),
			@Parameter(name = "type", description = "文件类型(1:人脸识别文件)")
	})
	@RequestMapping("/upload")
	public R upload(@RequestParam("file") MultipartFile file, String type) throws Exception {
		if (file.isEmpty()) {
			throw new EIException("上传文件不能为空");
		}
		String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
		File path = new File(ResourceUtils.getURL("classpath:static").getPath());
		if (!path.exists()) {
			path = new File("");
		}
		File upload = new File(path.getAbsolutePath(), "/upload/");
		if (!upload.exists()) {
			upload.mkdirs();
		}
		String fileName = new Date().getTime() + "." + fileExt;
		File dest = new File(upload.getAbsolutePath() + "/" + fileName);
		file.transferTo(dest);
		if (StringUtils.isNotBlank(type) && type.equals("1")) {
			ConfigEntity configEntity = configService.getOne(new QueryWrapper<ConfigEntity>().eq("name", "faceFile"));
			if (configEntity == null) {
				configEntity = new ConfigEntity();
				configEntity.setName("faceFile");
				configEntity.setValue(fileName);
			} else {
				configEntity.setValue(fileName);
			}
			configService.saveOrUpdate(configEntity);
		}
		return R.ok().put("file", fileName);
	}

	/**
	 * 文件下载接口
	 */
	@Operation(summary = "文件下载", description = "根据文件名下载指定文件")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "下载成功"),
			@ApiResponse(responseCode = "500", description = "下载失败或文件不存在")
	})
	@Parameter(name = "fileName", description = "要下载的文件名", required = true)
	@IgnoreAuth
	@RequestMapping("/download")
	public ResponseEntity<byte[]> download(@RequestParam String fileName) {
		try {
			File path = new File(ResourceUtils.getURL("classpath:static").getPath());
			if (!path.exists()) {
				path = new File("");
			}
			File upload = new File(path.getAbsolutePath(), "/upload/");
			if (!upload.exists()) {
				upload.mkdirs();
			}
			File file = new File(upload.getAbsolutePath() + "/" + fileName);
			if (file.exists()) {
				/*
				 * if(!fileService.canRead(file, SessionManager.getSessionUser())){
				 * getResponse().sendError(403);
				 * }
				 */
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				headers.setContentDispositionFormData("attachment", fileName);
				return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
