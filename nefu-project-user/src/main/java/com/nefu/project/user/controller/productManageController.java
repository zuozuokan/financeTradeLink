package com.nefu.project.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nefu.project.common.exception.productManager.ProductManagerException;
import com.nefu.project.common.result.HttpResult;
import com.nefu.project.domain.entity.Product;
import com.nefu.project.user.service.IProductManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @Operation(summary = "发布农产品")
    @PostMapping("add")
    public HttpResult addProduct(@RequestBody Product product){

        if(!IProductManageService.addProducts(product))
        {
            throw new ProductManagerException("添加失败");
        }
        return HttpResult.success(product);

    }
    @Operation(summary = "获取农产品")
    @GetMapping("{id}")
    public HttpResult getProductByUuid( @PathVariable("id") String uuid){
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
    @PostMapping("delete")
    public HttpResult deleteProduct(String Uuid){
        if( IProductManageService.deleteProductByUuid(Uuid)) {
            return HttpResult.success("删除成功");
        }
        else return  HttpResult.failed("删除失败");
    }





}
