package com.nefu.project.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.nefu.project.common.exception.order.StockException;
import com.nefu.project.common.exception.productManager.ProductManagerException;
import com.nefu.project.common.exception.user.DbException;
import com.nefu.project.domain.entity.Order;
import com.nefu.project.domain.entity.Product;
import com.nefu.project.domain.entity.User;
import com.nefu.project.user.mapper.IOrderManageMapper;
import com.nefu.project.user.mapper.IUserMapper;
import com.nefu.project.user.service.IOrderManageService;
import com.nefu.project.user.service.IProductManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class OrderManageServiceImpl implements IOrderManageService {
    @Autowired
    private IOrderManageMapper orderManageMapper;

    @Autowired
    private IProductManageService productManageService;

    @Autowired
    private IUserMapper  userMapper;

    @Override
    public Order findOrderByUuId(String Uuid) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getOrderUuid, Uuid);
// 按订单uuid查询
        Order orders = orderManageMapper.selectOne (queryWrapper);
        return orders;
    }

    //找用户对应的订单
    @Override
    public List<Order> findByUuid(String uuid) {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getOrderUserUuid, uuid);
// 按用户uuid查询
        List<Order> orders = orderManageMapper.selectList(queryWrapper);
        return orders;
    }

    //下单购买商品,创造订单
    @Transactional
    @Override
    public String createOrder(String ProductUuid, int amounts,String UserUuid) {
        Product product = productManageService.selectProductByUuid(ProductUuid);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserUuid, UserUuid);
        User user = userMapper.selectOne(queryWrapper);

   //     log.debug(product.toString());
        if(product==null||product.getProductStock()==0||product.getProductStock()<amounts){
            throw new StockException("库存不足");
        }

        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getUserUuid, user.getUserUuid());
        Order order = new Order();
        order.setOrderProductUuid(product.getProductUuid());
        order.setOrderQuantity(amounts);
        order.setOrderUuid(IdWorker.getIdStr());
        BigDecimal totalPrice = product.getProductPrice().multiply(new BigDecimal(amounts));
        log.debug("<UNK>"+totalPrice+"<UNK>");
        order.setOrderTotalPrice(totalPrice);
        order.setOrderUserUuid(UserUuid);
        product.setProductStock(product.getProductStock()-amounts);

        orderManageMapper.insert(order);

        productManageService.updateProduct(product);
        BigDecimal updatedAmount = user.getUserAmount().subtract(totalPrice);
        log.debug("<UNK>"+updatedAmount+"<UNK>");
        updateWrapper.set(User::getUserAmount,updatedAmount);
        userMapper.update(updateWrapper);//这是真正执行更新 没有这步 前面只是设置了新属性 却没有实际更新动作
        /*
        * 用户余额要改变
        * 操作这个行为的用户的uuid怎么获取
        * */
        return order.getOrderUuid();
    }
    //更新订单信息
    @Override
    public boolean updateOrder(String Uuid, Order order) {

        Order orders = findOrderByUuId(Uuid);
        if(orders == null){
            throw new ProductManagerException("未查找到该订单信息，请检查是否存在该订单");
        }
        log.debug("Existing Product: {}", orders); // 输出查询到的商品信息

        // 3. 使用 LambdaUpdateWrapper 来构建更新条件
        LambdaUpdateWrapper<Order> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Order::getOrderUuid, Uuid); // 根据 orderUuid 查找

        // 4. 只更新非空字段

        if (order.getOrderQuantity() != null) {
            updateWrapper.set(Order::getOrderQuantity, order.getOrderQuantity());
        }
        if(order.getOrderUserUuid() != null) {
            updateWrapper.set(Order::getOrderUserUuid,order.getOrderUserUuid());
        }
        if (order.getOrderTotalPrice() != null) {
            updateWrapper.set(Order::getOrderTotalPrice, order.getOrderTotalPrice());
        }
        if (order.getOrderStatus() != null) {
            updateWrapper.set(Order::getOrderStatus, order.getOrderStatus());
        }
        // 更新时间
        updateWrapper.set(Order::getOrderUpdatedTime, new Date()); // 更新时间

        // 5. 执行更新操作
        try {
            int rows = orderManageMapper.update(null, updateWrapper); // 执行更新操作
            log.debug("Rows affected: {}", rows);  // 输出受影响的行数
            return rows > 0; // 返回更新是否成功
        } catch (DbException e) {
            throw new DbException("数据库更新失败");
        }
    }

}
