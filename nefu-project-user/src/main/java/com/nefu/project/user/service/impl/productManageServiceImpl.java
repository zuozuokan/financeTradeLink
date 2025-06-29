package com.nefu.project.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.nefu.project.common.exception.productManager.ProductAddException;
import com.nefu.project.common.exception.productManager.ProductManagerException;
import com.nefu.project.common.exception.user.DbException;
import com.nefu.project.common.exception.user.UserRegistryException;
import com.nefu.project.domain.entity.Product;
import com.nefu.project.user.mapper.IProductManageMapper;
import com.nefu.project.user.service.IProductManageService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.reflect.Array.set;
@Slf4j
@Service
public class productManageServiceImpl implements IProductManageService {
    @Autowired
    private IProductManageMapper productManageMapper;
    @Override
    public void addProduct(Product product) {
        productManageMapper.insert(product);
    }

    @Override
    public List<Product> selectAllProduct() {
        return productManageMapper.selectList(null);
    }

    @Override
    public Product selectProductById(Integer id) {
        return productManageMapper.selectById(id);
    }

    @Override
    public void updateProduct(Product product) {
        productManageMapper.updateById(product);
    }

    //删除商品
    @Override
    public boolean deleteProductByUuid(String uuid) {
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Product::getProductUuid, uuid); // 根据 UUID 查找商品
        Product product = productManageMapper.selectOne(queryWrapper); // 查询商品

        // 如果商品不存在，返回 false
        if (product == null) {
            throw new ProductManagerException("未找到该商品有关信息，请检查信息是否填写正确");
        }
        // 2. 执行删除操作
        try {
            int rowsAffected = productManageMapper.delete(queryWrapper); // 删除操作
            if (rowsAffected > 0) {
                return true;
            }
        }catch (DbException e){
            throw new DbException("数据库产品操作异常");
        }
        return false;

    }
    //添加商品
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public boolean addProducts(Product product,String UserUuid) {
        // StringRedisTemplate redisTemplate;
       // String url= stringRedisTemplate.opsForValue().get(product.getProductImageUrl());
    //    log.info("<UNK>url<UNK>"+url);
        Product products = Product.builder()
                .productCategory(product.getProductCategory())
                .productName(product.getProductName())
                .productUuid(IdWorker.getIdStr())
                .productStock(product.getProductStock())
                .productPrice(product.getProductPrice())
                .productImageUrl(product.getProductImageUrl())
                .productDescription(product.getProductDescription())
                .productUserUuid(UserUuid)
          //      .productImageUrlTest(url)
                .productCreatedTime(new Date())
                .productUpdatedTime(new Date())
                .build();
        try{
            log.debug(product.getProductDescription());
            addProduct(products);
        }catch (Exception e){
            throw new ProductAddException("数据库添加失败");
        }

        return true;
    }

    //根据Uuid查找特定的产品
    @Override
    public Product selectProductByUuid(String uuid) {
        return productManageMapper.selectOne(
                new LambdaQueryWrapper<Product>()
                        .eq(Product::getProductUuid, uuid)
        );
    }
    //根据种类查取商品
    @Override
    public List<Product> selectAllProductByCategory(String category) {
        List<Product>productList = new ArrayList<>();
        try{
         productList = productManageMapper.selectList(new LambdaQueryWrapper<Product>().eq(Product::getProductCategory, category));}
        catch (Exception e){
            throw new DbException("数据库查询失败");
        }
        return productList;
    }

    //price数值类型为BigDecimal ，在JSON反序列化时，必须确保它传递了一个有效的值（即非空）。如果该字段在请求中为null或者没有传递，
    //（）例如为空或是没有提供该字段 会导致反序列化失败 抛出错误
    @Override
    public boolean updateProducts( Product product) {
        Product products = selectProductByUuid(product.getProductUuid());
        if(products == null){
            throw new ProductManagerException("未查找到该商品信息，请检查是否存在该商品");
        }
        log.debug("Existing Product: {}", products); // 输出查询到的商品信息

        LambdaUpdateWrapper<Product> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Product::getProductUuid, product.getProductUuid());
        // 4. 只更新非空字段
        if (product.getProductName() != null &&!product.getProductName().isEmpty()) {
            updateWrapper.set(Product::getProductName, product.getProductName());
        }
        if (product.getProductCategory() != null && !product.getProductCategory().isEmpty()) {
            updateWrapper.set(Product::getProductCategory, product.getProductCategory());
        }
        if (product.getProductDescription() != null && !product.getProductDescription().isEmpty()) {
            updateWrapper.set(Product::getProductDescription, product.getProductDescription());
        }
        if (product.getProductPrice() != null ) {
            updateWrapper.set(Product::getProductPrice, product.getProductPrice());
        }
        if (product.getProductStock() != null) {
            updateWrapper.set(Product::getProductStock, product.getProductStock());
        }
        if (product.getProductImageUrl() != null&&!product.getProductImageUrl().isEmpty() ) {
            updateWrapper.set(Product::getProductImageUrl, product.getProductImageUrl());
        }
        if(product.getProductUserUuid() != null && !product.getProductUserUuid().isEmpty()){
            updateWrapper.set(Product::getProductUserUuid, product.getProductUserUuid());
        }
        if (product.getProductStatus() != null && !product.getProductStatus().isEmpty()) {
            updateWrapper.set(Product::getProductStatus, product.getProductStatus());
        }
        //更新时间
        updateWrapper.set(Product::getProductUpdatedTime, new Date() );

        //执行更新操作

        try{
            int rows = productManageMapper.update(null, updateWrapper);
            log.debug("Rows affected: {}", rows);
            if(rows > 0){
                return true;
            }
        }catch (DbException e){
            throw new DbException("数据库更新失败");
        }
        return true;
    }

    @Override
    public String uploadImage(MultipartFile file, String objectName) {
            return "";
    }
}
