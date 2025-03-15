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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * 文件上传下载接口
 * 
 * @author xiaban
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/files")
@Tag(name = "文件管理", description = "文件上传下载相关接口")
public class FileController {
	@Autowired
	private ConfigService configService;

	private static final String UPLOAD_DIR = "/upload/";

	/**
	 * 文件上传接口
	 */
	@Operation(summary = "文件上传", description = "上传文件并返回文件名，支持人脸识别文件的特殊处理")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "上传成功"),
			@ApiResponse(responseCode = "400", description = "无效的请求"),
			@ApiResponse(responseCode = "500", description = "上传失败")
	})
	@Parameters({
			@Parameter(name = "file", description = "要上传的文件", required = true),
			@Parameter(name = "type", description = "文件类型(1:人脸识别文件)")
	})
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public R uploadFile(@RequestParam("file") MultipartFile file,
			@RequestParam(required = false) String type) throws Exception {
		if (file.isEmpty()) {
			throw new EIException("上传文件不能为空");
		}

		String fileName = generateFileName(file);
		File uploadedFile = saveFile(file, fileName);

		if (StringUtils.isNotBlank(type) && type.equals("1")) {
			updateFaceFileConfig(fileName);
		}

		return R.ok()
				.put("fileName", fileName)
				.put("fileUrl", "/api/v1/files/" + fileName);
	}

	/**
	 * 文件下载接口
	 */
	@Operation(summary = "文件下载", description = "根据文件名下载指定文件")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "下载成功"),
			@ApiResponse(responseCode = "404", description = "文件不存在"),
			@ApiResponse(responseCode = "500", description = "下载失败")
	})
	@GetMapping("/{fileName}")
	@IgnoreAuth
	public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
		try {
			File file = getFile(fileName);
			if (!file.exists()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

			return ResponseEntity.ok()
					.headers(createDownloadHeaders(fileName))
					.contentType(MediaType.APPLICATION_OCTET_STREAM)
					.body(FileUtils.readFileToByteArray(file));
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 私有辅助方法
	private String generateFileName(MultipartFile file) {
		String originalFileName = file.getOriginalFilename();
		String fileExt = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
		return new Date().getTime() + "." + fileExt;
	}

	private File saveFile(MultipartFile file, String fileName) throws IOException {
		File uploadDir = getUploadDirectory();
		File dest = new File(uploadDir, fileName);
		file.transferTo(dest);
		return dest;
	}

	private File getUploadDirectory() throws IOException {
		File path = new File(ResourceUtils.getURL("classpath:static").getPath());
		if (!path.exists()) {
			path = new File("");
		}
		File upload = new File(path.getAbsolutePath(), UPLOAD_DIR);
		if (!upload.exists()) {
			upload.mkdirs();
		}
		return upload;
	}

	private void updateFaceFileConfig(String fileName) {
		ConfigEntity configEntity = configService.getOne(
				new QueryWrapper<ConfigEntity>().eq("name", "faceFile"));
		if (configEntity == null) {
			configEntity = new ConfigEntity();
			configEntity.setName("faceFile");
		}
		configEntity.setValue(fileName);
		configService.saveOrUpdate(configEntity);
	}

	private File getFile(String fileName) throws IOException {
		File uploadDir = getUploadDirectory();
		return new File(uploadDir, fileName);
	}

	private HttpHeaders createDownloadHeaders(String fileName) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDispositionFormData("attachment", fileName);
		return headers;
	}
}
