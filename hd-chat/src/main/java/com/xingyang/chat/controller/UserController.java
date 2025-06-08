package com.xingyang.chat.controller;

import com.xingyang.chat.model.dto.UserUpdateDTO;
import com.xingyang.chat.model.vo.Result;
import com.xingyang.chat.model.vo.UserVO;
import com.xingyang.chat.service.FileService;
import com.xingyang.chat.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * User Controller
 *
 * @author XingYang
 */
@Slf4j
@Tag(name = "User API", description = "User related endpoints")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private FileService fileService;

    /**
     * Upload user avatar
     * 
     * @param file Avatar image file
     * @return Result with avatar URL
     */
    @Operation(summary = "Upload user avatar")
    @PostMapping("/avatar")
    public Result<Map<String, String>> uploadAvatar(@RequestParam("file") MultipartFile file) {
        log.info("============== Uploading avatar START ==============");
        log.info("Received file: name={}, size={} bytes, contentType={}", 
                file.getOriginalFilename(), file.getSize(), file.getContentType());
        
        if (file.isEmpty()) {
            log.error("Avatar file is empty");
            return Result.error("Avatar file is empty");
        }
        
        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            log.error("Invalid file type: {}", contentType);
            return Result.error("Invalid file type. Only images are allowed");
        }
        
        // 检查文件大小
        if (file.getSize() > 5 * 1024 * 1024) { // 5MB
            log.error("File size exceeds limit: {} bytes", file.getSize());
            return Result.error("File size exceeds the limit (5MB)");
        }
        
        try {
            // Upload file to MinIO storage
            log.info("Uploading file to MinIO, directory=avatars");
            String avatarUrl = fileService.uploadFile(file, "avatars");
            log.info("Avatar uploaded successfully, URL: {}", avatarUrl);
            
            // Update user's avatar in database
            log.info("Updating user's avatar in database");
            UserUpdateDTO updateDTO = new UserUpdateDTO();
            updateDTO.setAvatar(avatarUrl);
            UserVO updatedUser = userService.updateUserInfo(updateDTO);
            log.info("User avatar updated in database for user ID: {}", updatedUser.getId());
            
            // Return the avatar URL
            Map<String, String> result = new HashMap<>();
            result.put("avatarUrl", avatarUrl);
            
            log.info("============== Uploading avatar SUCCESS ==============");
            return Result.success("Avatar uploaded successfully", result);
        } catch (Exception e) {
            log.error("============== Uploading avatar FAILED ==============");
            log.error("Failed to upload avatar: {}", e.getMessage(), e);
            return Result.error("Failed to upload avatar: " + e.getMessage());
        }
    }
    
    /**
     * Update user profile
     * 
     * @param updateDTO User update data
     * @return Result with updated user information
     */
    @Operation(summary = "Update user profile")
    @PutMapping("/profile")
    public Result<UserVO> updateProfile(@RequestBody UserUpdateDTO updateDTO) {
        log.info("Updating profile for current user: {}", updateDTO);
        
        try {
            UserVO updatedUser = userService.updateUserInfo(updateDTO);
            return Result.success("Profile updated successfully", updatedUser);
        } catch (Exception e) {
            log.error("Failed to update profile: {}", e.getMessage(), e);
            return Result.error("Failed to update profile: " + e.getMessage());
        }
    }
} 