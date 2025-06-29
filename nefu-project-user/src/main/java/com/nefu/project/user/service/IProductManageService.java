package com.nefu.project.user.service;

import com.nefu.project.domain.entity.Product;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface IProductManageService {
    void addProduct(Product product); //添加农产品

    List<Product> selectAllProduct(); //获取农产品列表

    Product selectProductById(Integer id);  //获取商品详情

    Product selectProductByUuid(String uuid);

    void updateProduct(Product product); //更新商品

    boolean deleteProductByUuid(String uuid);     //删除商品

    boolean addProducts(Product product, String Uuid);//真正添加商品

    boolean updateProducts(Product product);

    List<Product> selectAllProductByCategory(String category);


    String uploadImage(MultipartFile file, String objectName);//上传图片返回url
}
