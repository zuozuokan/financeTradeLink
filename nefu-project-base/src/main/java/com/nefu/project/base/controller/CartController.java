package com.nefu.project.base.controller;

import com.nefu.project.common.exception.user.LoanApplicationException;
import com.nefu.project.common.result.HttpResult;
import com.nefu.project.domain.entity.Cart;
import com.nefu.project.base.service.ICartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车管理
 * 添加购物车
 * 删除购物车
 * 更新购物车商品数量
 * 查看我的购物车
 * 查看某一具体购物车项
 */

@Slf4j
@Tag(name = "购物车管理")
@RestController
@RequestMapping("/api/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;

    /**
     * 添加商品到购物车
     */
    @Operation(summary = "添加商品到购物车")
    @PostMapping("add")
    public HttpResult addCart(@RequestParam String userUuid, @RequestBody List<Cart> cartList) {
        iCartService.addCart(userUuid, cartList);
        return HttpResult.success("添加购物车成功");
    }

    /**
     * 删除购物车商品
     */
    @Operation(summary = "删除购物车商品")
    @PostMapping("delete")
    public HttpResult deleteCart(@RequestBody List<String> cartListUuid) {
        iCartService.deleteCart(cartListUuid);
        return HttpResult.success("删除购物车商品成功");
    }

    /**
     * 更新购物车商品数量
     */
    @Operation(summary = "更新购物车商品数量")
    @PostMapping("update/quantity")
    public HttpResult updateCartQuantity(@RequestParam String cartUuid, @RequestParam int quantity) {
        iCartService.updateCartQuantity(cartUuid, quantity);
        return HttpResult.success("更新购物车商品数量成功");
    }

    /**
     * 获取该用户的购物车列表
     */
    @Operation(summary = "获取该用户的购物车列表")
    @GetMapping("user-list")
    public HttpResult<List<Cart>> getCartList(@RequestParam String userUuid) {
        List<Cart> cartList = iCartService.getCartList(userUuid);
        return HttpResult.success(cartList);
    }

    /**
     * 查看购物车项详情
     */
    @Operation(summary = "查看购物车项详情")
    @GetMapping("/information")
    public HttpResult<Cart> getCart(@RequestParam String cartUuid) {
        Cart cart = iCartService.getCart(cartUuid);
        return HttpResult.success(cart);
    }

    @Operation(summary = "刷新商品缓存")
    @PostMapping("/refresh")
    public HttpResult<String> refreshCache(@RequestBody List<Cart> cartList) {
        // 这里可以添加刷新缓存的逻辑
        iCartService.refreshCartCache(cartList.stream()
                .map(Cart::getCartProductUuid)
                .toList());
        // 例如调用某个服务或方法来刷新缓存
        return HttpResult.success("商品缓存已刷新");
    }

}