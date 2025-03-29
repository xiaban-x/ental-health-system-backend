package com.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;

/**
 * Minio对象存储服务接口
 */
public interface MinioService {
    
    /**
     * 上传文件
     * @param file 文件
     * @param objectName 对象名称
     * @return 文件访问URL
     */
    String uploadFile(MultipartFile file, String objectName) throws Exception;
    
    /**
     * 获取文件访问URL
     * @param objectName 对象名称
     * @return 文件访问URL
     */
    String getFileUrl(String objectName) throws Exception;
    
    /**
     * 删除文件
     * @param objectName 对象名称
     */
    void deleteFile(String objectName) throws Exception;
    
    /**
     * 获取文件流
     * @param objectName 对象名称
     * @return 文件输入流
     */
    InputStream getObject(String objectName) throws Exception;
}