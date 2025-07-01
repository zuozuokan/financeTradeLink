package com.nefu.project.user.service;

import com.nefu.project.domain.entity.Order;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


public interface IOrderManageService {
    //Order findById( id);

    //Order findById(String Uuid);
    Order findOrderByUuId(String Uuid);//找特定订单的

    //Order findByUuId(String Uuid);

    List<Order> findByUuid(String uuid);
    String createOrder(String id,int amounts,String UserUuid);
    boolean updateOrder(String id,Order order);
    void updateSellerBalance(String sellerUuid, BigDecimal income);
}
