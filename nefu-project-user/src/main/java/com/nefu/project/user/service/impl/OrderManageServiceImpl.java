package com.nefu.project.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.nefu.project.common.exception.order.StockException;
import com.nefu.project.domain.entity.Order;
import com.nefu.project.domain.entity.Product;
import com.nefu.project.user.mapper.IOrderManageMapper;
import com.nefu.project.user.service.IOrderManageService;
import com.nefu.project.user.service.IProductManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class OrderManageServiceImpl implements IOrderManageService {
    @Autowired
    private IOrderManageMapper orderManageMapper;
    @Autowired
    private IProductManageService productManageService;


    @Override
    public Order findById(String Uuid) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getOrderUuid, Uuid);
// 按订单uuid查询
        Order orders = orderManageMapper.selectOne (queryWrapper);
        return orders;
    }

    @Override
    public List<Order> findByUuid(String uuid) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getOrderUserUuid, uuid);
// 按用户uuid查询
        List<Order> orders = orderManageMapper.selectList(queryWrapper);
        return orders;
    }

    @Transactional
    @Override
    public String createOrder(String id, int amounts) {
        Product product = productManageService.selectProductByUuid(id);
        log.debug(product.toString());
        if(product==null||product.getProductStock()==0||product.getProductStock()<amounts){
            throw new StockException("库存不足");
        }
        Order order = new Order();
        order.setOrderProductUuid(product.getProductUuid());
        order.setOrderQuantity(amounts);
        order.setOrderUuid(IdWorker.getIdStr());
        BigDecimal totalPrice = product.getProductPrice().multiply(new BigDecimal(amounts));
        order.setOrderTotalPrice(totalPrice);
        product.setProductStock(product.getProductStock()-amounts);
        orderManageMapper.insert(order);

        productManageService.updateProduct(product);

        /*
        * 用户余额要改变
        * 操作这个行为的用户的uuid怎么获取
        * */
        return order.getOrderUuid();
    }
}
