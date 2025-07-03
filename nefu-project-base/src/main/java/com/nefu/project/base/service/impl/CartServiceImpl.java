package com.nefu.project.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.nefu.project.base.mapper.IUserMapper;
import com.nefu.project.base.service.IProductCacheService;
import com.nefu.project.common.exception.user.CartException;
import com.nefu.project.common.exception.user.DbException;
import com.nefu.project.common.exception.user.LoanApplicationException;
import com.nefu.project.common.exception.user.UserException;
import com.nefu.project.domain.entity.Cart;
import com.nefu.project.domain.entity.User;
import com.nefu.project.base.mapper.ICartMapper;

import com.nefu.project.base.service.ICartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 购物车管理，确保所有商品都已经存在
 * 删除购物车
 * 更新购物车商品数量
 * 查看我的购物车
 * 查看某一具体购物车项
 */

@Slf4j
@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    ICartMapper iCartMapper;

    @Autowired
    IUserMapper iUserMapper;

    @Autowired
    IProductCacheService productCacheService;

    /**
     * description 添加商品到购物车
     *
     * @params [userUuid, cart]
     * @return void
     */

    @Override
    public void addCart(String userUuid, List<Cart> cartList) {
        stringIsExist(userUuid, "用户ID为空");
        // 购物车商品ID非空约束
        if(cartList.isEmpty()||cartList.size()==0){
            throw new CartException("需要添加购物车商品为空");
        }

        // 查找用户，关联用户约束
        User user = iUserMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .select(User::getUserId)
                        .eq(User::getUserUuid, userUuid)
        );
        if (Objects.isNull(user)) {
            throw new UserException("用户不存在,添加购物车失败");
        }
        // 按商品分组，合并重复商品
        Map<String, Cart> productMap = new HashMap<>();
        for (Cart cart : cartList) {
            // 单个商品校验
            stringIsExist(cart.getCartProductUuid(),"购物车商品ID不能为空");
            if ( cart.getCartQuantity() <= 0) {
                throw new LoanApplicationException("购物车商品数量必须大于0");
            }
            // 合并相同商品的数量
            productMap.compute(cart.getCartProductUuid(), (producctUuid, v) -> {
                if (v == null) {
                    return cart;
                } else {
                    v.setCartQuantity(v.getCartQuantity() + cart.getCartQuantity());
                    return v;
                }
            });

        }
        log.debug("productMap:{}", productMap);
         //检测关联商品
        List<String> productUuids = new ArrayList<>(productMap.keySet());
        String missingProductUuids = "";

        if (!productUuids.isEmpty()) {
            //获取所有已经存在的
           Map<String,Boolean> existProductMap = productCacheService.batchCheckProductExists(productUuids) ;
            //检查商品是否存在
           for (String productUuid : productUuids) {
               if (!Boolean.TRUE.equals(existProductMap.get(productUuid))) {
                   missingProductUuids +=  "未找到商品ID为"+productUuid+"的商品；";
               }
           }
        }
        log.debug("productUuids:{}", productUuids);
        if(!missingProductUuids.trim().isEmpty()){
            log.debug("missingProductUuids:{}",missingProductUuids);
            throw new CartException(missingProductUuids);
        }

        // 检查商品是否已在购物车中
        List<Cart> existingCarts = iCartMapper.selectList(
                new LambdaQueryWrapper<Cart>()
                        .select(Cart::getCartId, Cart::getCartProductUuid, Cart::getCartQuantity)
                        .eq(Cart::getCartUserUuid, userUuid)
                        .in(Cart::getCartProductUuid,productUuids)
        );
         log.debug("existingCarts:{}", existingCarts);
        //转换为Map,方便查询
        Map<String, Cart> existingCartMap = existingCarts.stream().collect(Collectors.toMap(Cart::getCartProductUuid, c->c));
        log.debug("已经在购物车的existingCartMap:{}", existingCartMap);
        //构建更新和插入操作
        for(Map.Entry<String, Cart> entry : productMap.entrySet()){
                String productUuid = entry.getKey();
                Cart newCart = entry.getValue();

                //更新
                if(existingCartMap.containsKey(productUuid)){
                    int  quantity = existingCartMap.get(productUuid).getCartQuantity()+newCart.getCartQuantity();
                   newCart.setCartQuantity(quantity);
                   newCart.setCartUpdatedTime(new Date());
                    LambdaUpdateWrapper<Cart> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(Cart::getCartId, existingCartMap.get(productUuid).getCartId())
                            .set(Cart::getCartQuantity, newCart.getCartQuantity());
                    try{
                        int rows = iCartMapper.update(null, lambdaUpdateWrapper);
                        if (rows == 0) {
                            throw new CartException("购物车中不存在ID为"+ existingCartMap.get(productUuid).getCartUuid());
                        }
                    }catch (DbException e){
                        throw new DbException(e.getMessage());
                    }
                }
                else{//插入
                    newCart.setCartUuid(String.valueOf(IdWorker.getId()));
                    newCart.setCartUserUuid(userUuid);
                    newCart.setCartCreatedTime(new Date());
                    newCart.setCartUpdatedTime(new Date());
                    iCartMapper.insert(newCart);
                }
        }
    }

    /**
     * description 删除购物车商品
     *
     * @params [cartUuid]
     * @return void
     */
    @Override
    public void deleteCart(List<String> cartListUuid) {
        String exceptionListUuid="";
        log.debug("cartListUuid:"+cartListUuid);
        for(String cartUuid : cartListUuid){
            // 传入的参数不能为空值，购物车ID
            stringIsExist(cartUuid, "购物车不存在ID为"+cartUuid);
            log.debug("cartUuid:{}",cartUuid);
            // 关联性查验
            Cart cart = iCartMapper.selectByUuid(cartUuid);
            log.debug("car:{}",cart);
            if (cart == null) {
                exceptionListUuid += "ID为的"+cartUuid+"购物车项目不存在；";

            }else {
                // 删除购物车项
                try {
                    int removed = iCartMapper.deleteById(cart.getCartId());
                    if (removed == 0) {
                        throw new LoanApplicationException("未知原因购物车项删除失败");
                    }
                } catch (DbException e) {
                    throw new DbException(e.getMessage());
                }
            }
        }
        if(!exceptionListUuid.trim().isEmpty()){
            log.debug("exceptionListUuid:{}",exceptionListUuid);
            throw new LoanApplicationException(exceptionListUuid);
        }

    }

    /**
     * description 更新购物车商品数量
     *
     * @params [cartUuid, quantity]
     * @return void
     */
    @Override
    public void updateCartQuantity(String cartUuid, int quantity) {
        stringIsExist(cartUuid, "购物车ID为空");
        if (Objects.isNull(quantity) || quantity <= 0) {
            throw new LoanApplicationException("购物车商品数量必须大于0");
        }
        LambdaUpdateWrapper<Cart> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Cart::getCartUuid, cartUuid)
                .set(Cart::getCartQuantity, quantity)
                .set(Cart::getCartUpdatedTime, new Date());
        try {
            int rows = iCartMapper.update(null, lambdaUpdateWrapper);
            if (rows == 0) {
                throw new LoanApplicationException("没有该购物车项，数量更新失败");
            }
        } catch (DbException e) {
            throw new DbException(e.getMessage());
        }
    }

    /**
     * description 获取该用户的购物车列表
     *
     * @params [userUuid]
     * @return java.util.List<com.nefu.project.domain.entity.Cart>
     */
    @Override
    public List<Cart> getCartList(String userUuid) {
        // 传入值为空
        stringIsExist(userUuid, "无法正常获取用户ID");
        // 查询数据库
        try {
            List<Cart> cartList = iCartMapper.getCartListByUserUuid(userUuid);
            if (cartList.size() == 0) {
                throw new LoanApplicationException("该用户购物车为空");
            }
            log.debug("cartList.size()=" + cartList.size());
            return cartList;
        } catch (DbException e) {
            throw new DbException(e.getMessage());
        }
    }

    /**
     * description 查看这个购物车项的详细内容
     *
     * @params [cartUuid]
     * @return com.nefu.project.domain.entity.Cart
     */
    @Override
    public Cart getCart(String cartUuid) {
        stringIsExist(cartUuid, "购物车ID为空");
        Cart cart = iCartMapper.selectByUuid(cartUuid);
        if (cart == null) {
            throw new CartException("该商品不存在");
        }
        return cart;
    }

    /**
     * @param productUuids
     */
    @Override
    public void refreshCartCache(List<String> productUuids) {
        if (productUuids == null || productUuids.isEmpty()) {
            return; // 无商品，直接返回
        }
        productCacheService.clearProductCacheBatch(productUuids);
    }


    /**
     * description 判断字符是否有值
     *
     * @params [string, message]
     * @return void
     */
    public void stringIsExist(String string, String message) {
        if (string == null || string.trim().isEmpty()) {
            throw new LoanApplicationException(message);
        }
    }




}