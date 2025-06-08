package com.xingyang.chat.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * File Service
 *
 * @author XingYang
 */
public interface FileService {

    /**
     * Upload file to storage
     *
     * @param file File to upload
     * @param directory Directory to store the file
     * @return File URL
     * @throws Exception if upload fails
     */
    String uploadFile(MultipartFile file, String directory) throws Exception;
    
    /**
     * Delete file from storage
     *
     * @param fileUrl File URL to delete
     * @return true if deletion is successful
     * @throws Exception if deletion fails
     */
    boolean deleteFile(String fileUrl) throws Exception;
} 