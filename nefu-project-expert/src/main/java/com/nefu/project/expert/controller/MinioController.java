package com.nefu.project.expert.controller;

import com.nefu.project.common.result.HttpResult;
import com.nefu.project.expert.service.IMinioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Tag(name="minio测试")
@RestController
@RequestMapping("/api/minio")
public class MinioController {

    @Autowired
    private IMinioService minioService;

    /**
     * 上传图片（自动生成文件名）
     */
    @PostMapping("/upload")
    public   HttpResult<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String objectName = minioService.uploadImage(file);
        return HttpResult.success(objectName);
    }

    /**
     * 上传图片（自定义文件名）
     */
    @PostMapping("/upload/imagename")
    public HttpResult uploadImageWithName(
            @RequestParam("file") MultipartFile file,
            @RequestPart String customName) {
       String objectName = minioService.uploadImage(file, customName);
        return HttpResult.success(objectName);
    }

    /**
     * 删除图片
     */
    @PostMapping("/delete")
    public HttpResult deleteImage(@RequestParam String url) {
        minioService.deleteImage(url);
        return HttpResult.success(url);
    }

    /**
     * 获取图片URL
     */
    @GetMapping("/url")
    public HttpResult getImageUrl(@RequestParam String objectName) {
       return HttpResult.success(minioService.getImageUrl(objectName));
    }
    @GetMapping("/urlex")
    public HttpResult getExpertUrl(@RequestParam String objectName) {
        return HttpResult.success(minioService.isImageExist(objectName));
    }

}