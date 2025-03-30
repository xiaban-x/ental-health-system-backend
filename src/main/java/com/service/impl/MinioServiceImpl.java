package com.service.impl;

import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.service.MinioService;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Minio对象存储服务实现类
 */
@Service("minioService")
public class MinioServiceImpl implements MinioService {

        @Autowired
        private MinioClient minioClient;

        @Value("${minio.bucket.name}")
        private String bucketName;

        @Value("${minio.bucket.chunks}")
        private String chunkBucketName;

        /**
         * 上传文件
         */
        @Override
        public String uploadFile(MultipartFile file, String objectName) throws Exception {
                minioClient.putObject(
                                PutObjectArgs.builder()
                                                .bucket(bucketName)
                                                .object(objectName)
                                                .stream(file.getInputStream(), file.getSize(), -1)
                                                .contentType(file.getContentType())
                                                .build());
                return getFileUrl(objectName);
        }

        /**
         * 获取文件访问URL
         */
        @Override
        public String getFileUrl(String objectName) throws Exception {
                return minioClient.getPresignedObjectUrl(
                                GetPresignedObjectUrlArgs.builder()
                                                .bucket(bucketName)
                                                .object(objectName)
                                                .method(Method.GET)
                                                .expiry(7, TimeUnit.DAYS)
                                                .build());
        }

        /**
         * 删除文件
         */
        @Override
        public void deleteFile(String objectName) throws Exception {
                // 判断文件路径是否以chunks/开头，如果是则使用chunkBucketName
                String bucket = objectName.startsWith("chunks/") ? chunkBucketName : bucketName;

                minioClient.removeObject(
                                RemoveObjectArgs.builder()
                                                .bucket(bucket)
                                                .object(objectName)
                                                .build());
        }

        /**
         * 删除指定桶中的文件
         */
        @Override
        public void deleteFile(String bucketName, String objectName) throws Exception {
                minioClient.removeObject(
                                RemoveObjectArgs.builder()
                                                .bucket(bucketName)
                                                .object(objectName)
                                                .build());
        }

        /**
         * 获取文件流
         */
        @Override
        public InputStream getObject(String objectName) throws Exception {
                return minioClient.getObject(
                                GetObjectArgs.builder()
                                                .bucket(bucketName)
                                                .object(objectName)
                                                .build());
        }
}