package com.nefu.project.user.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nefu.project.common.result.HttpResult;
import com.nefu.project.domain.entity.Order;


import com.nefu.project.user.service.IOrderManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Tag(name="订单管理")
@RestController
@RequestMapping("/api/order/")
public class OrderManagerController {

    @Autowired
    private IOrderManageService orderManageService;

    @Operation(summary = "下单购买商品")
    @PostMapping("create")
    public HttpResult create(String productUuid,int amounts,String userUuid){

        orderManageService.createOrder(productUuid ,amounts,userUuid);
        return HttpResult.success("购买成功");
    }

    @Operation(summary = "查看订单详情")
    @GetMapping("{id}")
    public HttpResult getOrderFindByUuId( @PathVariable("id") String Uuid){
        Order order = orderManageService.findOrderByUuId(Uuid);
        if(order!=null){
            return HttpResult.success(order);
        }
         return  HttpResult.success(order);
    }

    @Operation(summary = "获取用户订单详情")
    @GetMapping("my")
    public HttpResult getAllOrdersByToken(String userUuid){
        //@RequestParam String token
           List<Order> orders = new ArrayList<>();
        try
        {
            /**
             *
             *   // 1. 解析 token，获取 uuid
             *             Algorithm algorithm = Algorithm.HMAC256("project_11_group"); // 替换为你的实际 SALT
             *             JWTVerifier verifier = JWT.require(algorithm).build();
             *             DecodedJWT jwt = verifier.verify(token);
             *             String uuid = jwt.getClaim("uuid").asString();
             */

            // 2. 查询订单列表（假设 order 表有 order_user_uuid 字段）
          orders   = orderManageService.findByUuid(userUuid);
        }
        catch (Exception e) {
            return HttpResult.failed("查询失败："+ e.getMessage());
        }
        return HttpResult.success(orders);
    }

    @Operation(summary = "更新订单状态")
    @PutMapping("update/orderUuid")
    public HttpResult updateOrder( String Uuid, @RequestBody Order order){
        if(orderManageService.updateOrder(Uuid,order)){
            return HttpResult.success("更新成功");
        }
        return HttpResult.failed("更新失败");
    }


}
