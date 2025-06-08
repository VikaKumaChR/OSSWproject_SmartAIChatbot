package com.xingyang.chat.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import com.xingyang.chat.model.vo.Result;
import com.xingyang.chat.service.CaptchaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Captcha Controller
 *
 * @author XingYang
 */
@Slf4j
@Tag(name = "Captcha API", description = "Endpoints for captcha generation and validation")
@RestController
@RequestMapping("/captcha")
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;

    /**
     * Generate a captcha
     *
     * @return Captcha data including base64 image and uuid
     */
    @Operation(summary = "Generate Captcha", description = "Generate a new captcha image with a unique identifier")
    @GetMapping("/generate")
    public Result<Map<String, String>> generateCaptcha() {
        log.info("Generating new captcha");
        try {
            // Generate a unique identifier for this captcha
            String uuid = IdUtil.simpleUUID();
            log.debug("Generated UUID: {}", uuid);
            
            // Generate the captcha image
            log.debug("Calling captcha service to generate image");
            BufferedImage image = captchaService.generateCaptcha(uuid);
            
            if (image == null) {
                log.error("Captcha service returned null image");
                return Result.error("Failed to generate captcha image");
            }
            
            log.debug("Captcha image generated, width: {}, height: {}", image.getWidth(), image.getHeight());
            
            // Convert the image to base64 - using PNG instead of JPG
            String base64Image;
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                // 使用PNG格式替代JPG
                boolean success = ImageIO.write(image, "png", outputStream);
                if (!success) {
                    log.error("Failed to write image to output stream - no appropriate writer found");
                    return Result.error("Failed to encode captcha image");
                }
                
                byte[] imageBytes = outputStream.toByteArray();
                log.debug("Image converted to byte array, size: {} bytes", imageBytes.length);
                
                // 更改为PNG格式的数据URL
                base64Image = "data:image/png;base64," + Base64.encode(imageBytes);
                log.debug("Base64 image string length: {}", base64Image.length());
            }
            
            // Return the captcha data
            Map<String, String> captchaData = new HashMap<>(2);
            captchaData.put("uuid", uuid);
            captchaData.put("img", base64Image);
            
            log.info("Captcha generated successfully with UUID: {}", uuid);
            return Result.success(captchaData);
        } catch (IOException e) {
            log.error("Failed to generate captcha: {}", e.getMessage(), e);
            return Result.error("Failed to generate captcha: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error generating captcha: {}", e.getMessage(), e);
            return Result.error("System error while generating captcha: " + e.getMessage());
        }
    }
} 