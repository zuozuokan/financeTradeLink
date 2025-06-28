package com.nefu.project.user.service;

import org.springframework.web.multipart.MultipartFile;

public interface IMinioService {
    String uploadImage(MultipartFile file, String objectName);
    String uploadImage(MultipartFile file);
    void deleteImage(String imageUrl);
    String getImageUrl(String objectName);
    Boolean isImageExist(String imageUrl);
}