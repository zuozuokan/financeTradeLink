package com.nefu.project.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.nefu.project.common.exception.productManager.ProductAddException;
import com.nefu.project.common.exception.productManager.ProductManagerException;
import com.nefu.project.common.exception.user.DbException;
import com.nefu.project.common.exception.user.UserRegistryException;
import com.nefu.project.domain.entity.Product;
import com.nefu.project.user.mapper.IProductManageMapper;
import com.nefu.project.user.service.IProductManageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
            return false;
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

    @Override
    public boolean addProducts(Product product) {
        Product products = Product.builder()
                .productCategory(product.getProductCategory())
                .productName(product.getProductName())
                .productUuid(IdWorker.getIdStr())
                .productCategory(product.getProductCategory())
                .productPrice(product.getProductPrice())
                .productCreatedTime(new Date())
                .productUpdatedTime(new Date())
                .build();
        try{
            addProduct(products);
        }catch (Exception e){
            throw new ProductAddException("数据库添加失败");
        }

        return true;
    }

    @Override
    public Product selectProductByUuid(String uuid) {
        return productManageMapper.selectOne(
                new LambdaQueryWrapper<Product>()
                        .eq(Product::getProductUuid, uuid)
        );
    }
}
