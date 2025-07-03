package com.nefu.project.base.service;


import com.nefu.project.domain.entity.Cart;

import java.util.List;

public interface ICartService {

    void addCart(String userUuid,List<Cart> cart);
    void deleteCart(List<String> CartUuid);
    void updateCartQuantity(String CartUuid,int quantity);
    List<Cart> getCartList(String userUuid);
    Cart getCart(String cartUuid);
    // 刷新购物车中商品缓存

    void refreshCartCache(List<String> productUuids);
}
