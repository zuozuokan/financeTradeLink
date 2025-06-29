package com.nefu.project.user.service.impl;

import com.nefu.project.common.exception.user.LoanApplicationException;
import com.nefu.project.user.service.IMinioService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.errors.ErrorResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@Slf4j
public class MinioServiceImpl implements IMinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Value("${minio.image-folder}")
    private String imageFolder;

    @Value("${minio.endpoint}")
    private String endpoint;

    // 构造器注入MinioClient
    public MinioServiceImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }



    /**
     * description 上传文件
     *
     * @params [file]
     * @return java.lang.String
     */
    @Override
    public String uploadImage(MultipartFile file) {
        String extension = getFileExtension(file.getOriginalFilename());
        String objectName = imageFolder + "/" + UUID.randomUUID() + "." + extension;
        String url = generateUrl(objectName);
        log.debug("urlupload: " + url);
        if(isImageExist(url)){
            throw new LoanApplicationException("该url已经存在");
        }
        try (InputStream is = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(is, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return url;
        } catch (Exception e) {
            throw new RuntimeException("上传失败: " + e.getMessage(), e);
        }
    }

    /**
     * description 上传自定义文件，默认为jpg格式
     *
     * @params [file, fileName]
     * @return java.lang.String
     */
    @Override
    public String uploadImage(MultipartFile file, String fileName) {
        //后缀处理
        String imageName = imageFolder + "/" ;
        String extension = getFileExtension(file.getOriginalFilename());
        if(fileName == null||fileName.trim().equals("")){
            imageName +=  UUID.randomUUID() + "." + extension;
        }
        int lastIndex = fileName.lastIndexOf(".");
        if(lastIndex == -1||lastIndex == fileName.length()-1){
            imageName += fileName.toLowerCase() + "." + extension;
        }else{
        imageName += fileName.toLowerCase();
        }

        String url = generateUrl(imageName);
        log.debug("url: " + url);
        if(isImageExist(url)){
            throw new LoanApplicationException("该url已经存在");
        }
        try (InputStream is = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(imageName)
                            .stream(is, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return url;
        } catch (Exception e) {
            throw new RuntimeException("上传失败: " + e.getMessage(), e);
        }
    }

    // 删除图片（支持URL或对象名）
    @Override
    public void deleteImage(String urlOrObjectName) {
        try {
            // 1. 解析URL获取对象名（兼容路径和查询参数）
            String objectName = extractObjectName(urlOrObjectName);
            if (objectName == null) {
                throw new LoanApplicationException("无效的URL或对象名: " + urlOrObjectName);
            }
            // 2. 校验对象存在性（
            if (!isImageExist(generateUrl(objectName))) {
                log.warn("尝试删除不存在的对象: {}", objectName);
                throw new LoanApplicationException("对象不存在: " + objectName);
            }
            // 3. 执行删除操作
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            log.info("成功删除对象: {}", objectName);

        } catch (ErrorResponseException e) {
            // 处理MinIO服务端错误
            if ("NoSuchKey".equals(e.errorResponse().code())) {
                log.error("对象不存在: {}", urlOrObjectName);
                throw new LoanApplicationException("对象不存在: " + urlOrObjectName);
            }
            log.error("MinIO服务端错误: {}", e.errorResponse().code(), e);
            throw new LoanApplicationException("删除失败: 服务端错误");
        } catch (Exception e) {
            log.error("删除对象失败: {}", urlOrObjectName, e);
            throw new LoanApplicationException("删除失败: " + e.getMessage());
        }
    }


    /**
     * description 获取已经存在的URL
     *
     * @params [fileName]
     * @return java.lang.String
     */
    @Override
    public String getImageUrl(String fileName) {
        String extension = getFileExtension(fileName);
        String imageName = imageFolder + "/" + fileName+"."+extension;
         String url = generateUrl(imageName);
         if (!isImageExist(url)){
             throw new LoanApplicationException("文件不存在");
         }
         return url;
    }

    /**
     * description 判断URL是否存在
     *
     * @params [imageUrl]
     * @return java.lang.Boolean
     */
    @Override
    public Boolean isImageExist(String imageUrl) {
        try {
            String prefix = endpoint.endsWith("/") ? endpoint : endpoint + "/";
            prefix += bucketName + "/";
            String objectName = null;
            if (imageUrl.startsWith(prefix)) {
                String pro = imageUrl.substring(prefix.length());
                try {
                    // 捕获解码异常
                    objectName = URLDecoder.decode(pro, StandardCharsets.UTF_8);
                    log.debug("解码后对象名: " + objectName);
                } catch (IllegalArgumentException e) {
                    log.error("URL解码失败: " + pro, e);
                    return false;
                }
            }
            if (objectName == null) {
                return false;
            }
            // 验证对象存在性
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            return true;

        } catch (Exception e) {
            log.error("MinIO检查失败: " + imageUrl, e);
            return false;
        }

    }
    /**
     * description 生成URL
     *
     * @params [fileName]
     * @return java.lang.String
     */
    // 修改generateUrl方法，保留路径分隔符
    private String generateUrl(String fileName) {
        String encoded = fileName
                .replace(" ", "%20")
                .replace("<", "%3C")
                .replace(">", "%3E")
                .replace("#", "%23")
                .replace("%", "%25")
                .replace("|", "%7C");
        // 保留原生/符号，不进行编码
        return endpoint + "/" + bucketName + "/" + encoded;
    }
    /**
     * description 后缀进行处理
     *
     * @params [filename]
     * @return java.lang.String
     */
    private String getFileExtension(String filename) {
        if (filename == null) {
            return "jpg";
        }
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return "jpg"; // 无扩展名或最后一个字符是点
        }
        return filename.substring(lastDotIndex + 1).toLowerCase(); // 统一小写
    }
    /**
     * description 从URL中提取对象名
     *
     * @params [url]
     * @return java.lang.String
     */
    private String extractObjectName(String url) {
        if (!url.startsWith("http")) {
            System.out.println("直接返回对象名: " + url);
            return url;
        }
        String prefix = endpoint.endsWith("/") ? endpoint : endpoint + "/";
        prefix += bucketName + "/";
        System.out.println("期望前缀: " + prefix); // 调试：检查前缀是否正确
        if (url.startsWith(prefix)) {
            String objectName = url.substring(prefix.length());
            try {
                String decoded = URLDecoder.decode(objectName, StandardCharsets.UTF_8);
                System.out.println("解码后对象名: " + decoded);
                return decoded;
            } catch (RuntimeException e) {
                throw new RuntimeException("URL解码失败: " + objectName, e);
            }
        }
        throw new IllegalArgumentException("无效URL: " + url + "，需符合格式：" + prefix + "xxx");
    }

}