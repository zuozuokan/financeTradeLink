package com.nefu.project.user.service;

import com.nefu.project.domain.entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IOrderManageService {
    //Order findById( id);

    Order findById(String Uuid);

    List<Order> findByUuid(String uuid);
    String createOrder(String id,int amounts);
}
