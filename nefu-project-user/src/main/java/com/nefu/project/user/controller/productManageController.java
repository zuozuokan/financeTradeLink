package com.nefu.project.user.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SmUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nefu.project.common.exception.productManager.ProductManagerException;
import com.nefu.project.common.exception.productManager.UploadException;
import com.nefu.project.common.result.HttpResult;
import com.nefu.project.domain.entity.Product;
import com.nefu.project.user.service.IProductManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Tag(name= "农产品更新测试")
@RestController
@RequestMapping("/api/product/")
public class productManageController {
    @Autowired
    private IProductManageService IProductManageService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Operation(summary = "发布农产品样式图")
    @PostMapping("addPictures")
    @SneakyThrows
    public HttpResult<String> addProductPicture(@RequestPart("file" ) MultipartFile file) {
        String contentType = file.getContentType();
        if(!contentType.contains("image")){
            throw new UploadException("图片格式不正确，上传失败");
        }
        String encode = Base64.encode(file.getInputStream());
    //    String base64Image = "data:" + contentType + ";base64," + encode;
        String nCode = SmUtil.sm3(encode);//摘要为64位

        Boolean hasImage = stringRedisTemplate.hasKey(nCode);
        if(hasImage) throw new UploadException("图片已经存在，上传失败");
        stringRedisTemplate.opsForValue().set(nCode,encode);
        return HttpResult.success(nCode);

    }
    @Operation(summary = "添加农产品")
    @PostMapping("addProduct")
    public HttpResult addProduct(@RequestBody Product product,String userUuid ){
//         String nCode = stringRedisTemplate.opsForValue().get(product.getProductImageUrl());
//         product.setProductImageUrl(nCode);
        if(!IProductManageService.addProducts(product,userUuid))
        {
            throw new ProductManagerException("添加失败");
        }

        return HttpResult.success(product);

    }
    @Operation(summary = "获取农产品")
    @GetMapping("{productUuid}")
    public HttpResult getProductByUuid( @PathVariable("productUuid") String uuid){
        log.info("getProductByUuid:{}",uuid);
        Product product = IProductManageService.selectProductByUuid(uuid);
        log.info("商品:{}",product);
        if(product == null){
            throw new ProductManagerException("未查找到该商品的相关信息，请检查输入信息是否有误");
        } else if (product.getProductStock()==0) {
            throw new ProductManagerException("该商品库存为0");
        }
        return HttpResult.success(product);
    }
    @Operation(summary = "根据种类获取商品")
    @GetMapping("category")
    public HttpResult selectAllProductByCategory(String category){
        log.info("selectAllProductByCategory:{}",category);
        List<Product> products = IProductManageService.selectAllProductByCategory(category);
        if(products == null || products.isEmpty()){//返回空列表是empty
            return HttpResult.failed("该种类商品为空");
        }
        return HttpResult.success(products);
    }
    @Operation(summary = "获取农产品列表")
    @GetMapping("list")
    public HttpResult listProduct(){
        List<Product> products = IProductManageService.selectAllProduct();
        if(products.isEmpty()) {
        //   throw  new ProductManagerException("蔬菜销售完毕，库存为空");
           return HttpResult.success("蔬菜为空");
        }
        return HttpResult.success(products);
    }
    @Operation(summary = "删除/下架商品")
//    hahah
    @PostMapping("delete")
    public HttpResult deleteProduct(String productUuid){
        if( IProductManageService.deleteProductByUuid(productUuid)) {
            return HttpResult.success("删除成功");
        }
        else return  HttpResult.failed("删除失败");
    }

    @Operation(summary = "更新商品")
    @PostMapping("update")
    public HttpResult updateProduct(@RequestBody Product product){
        IProductManageService.updateProducts(product);
        return HttpResult.success(product);
    }





}
