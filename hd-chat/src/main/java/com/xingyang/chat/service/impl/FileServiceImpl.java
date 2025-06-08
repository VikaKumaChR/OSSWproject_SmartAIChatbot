package com.xingyang.chat.service.impl;

import com.xingyang.chat.service.FileService;
import io.minio.*;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * File Service Implementation using MinIO
 *
 * @author XingYang
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;
    
    @Value("${minio.avatarBucketName}")
    private String avatarBucketName;

    @Value("${minio.endpoint}")
    private String minioEndpoint;

    @Override
    public String uploadFile(MultipartFile file, String directory) throws Exception {
        try {
            // 确定要使用的bucket
            String targetBucket = "avatars".equals(directory) ? avatarBucketName : bucketName;
            
            // Check if bucket exists
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(targetBucket).build());
            if (!bucketExists) {
                // Create bucket if it doesn't exist
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(targetBucket).build());
                log.info("Created bucket: {}", targetBucket);
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = UUID.randomUUID().toString().replace("-", "") + extension;
            
            // Construct object path
            String objectName = filename;
            
            // Upload file
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(targetBucket)
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );
            
            log.info("File uploaded successfully: {}/{}", targetBucket, objectName);
            
            // Construct and return file URL
            return minioEndpoint + "/" + targetBucket + "/" + objectName;
            
        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException | IOException e) {
            log.error("Error uploading file to MinIO: {}", e.getMessage(), e);
            throw new Exception("Failed to upload file: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteFile(String fileUrl) throws Exception {
        try {
            // Extract bucket and object name from URL
            String[] parts = extractBucketAndObjectFromUrl(fileUrl);
            if (parts == null || parts.length != 2) {
                return false;
            }
            
            String targetBucket = parts[0];
            String objectName = parts[1];
            
            // Delete object
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(targetBucket)
                    .object(objectName)
                    .build()
            );
            
            log.info("File deleted successfully: {}/{}", targetBucket, objectName);
            return true;
            
        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException | IOException e) {
            log.error("Error deleting file from MinIO: {}", e.getMessage(), e);
            throw new Exception("Failed to delete file: " + e.getMessage(), e);
        }
    }
    
    /**
     * 从URL中提取bucket名称和对象名称
     * 
     * @param fileUrl 文件URL
     * @return 包含bucket和object的数组，如果解析失败则返回null
     */
    private String[] extractBucketAndObjectFromUrl(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return null;
        }
        
        // 移除endpoint前缀
        String urlWithoutEndpoint = fileUrl;
        if (fileUrl.startsWith(minioEndpoint)) {
            urlWithoutEndpoint = fileUrl.substring(minioEndpoint.length());
        }
        
        // 确保URL以/开头
        if (!urlWithoutEndpoint.startsWith("/")) {
            urlWithoutEndpoint = "/" + urlWithoutEndpoint;
        }
        
        // 分割路径
        String[] pathParts = urlWithoutEndpoint.split("/", 3);
        if (pathParts.length < 3) {
            return null;
        }
        
        return new String[] { pathParts[1], pathParts[2] };
    }
} 