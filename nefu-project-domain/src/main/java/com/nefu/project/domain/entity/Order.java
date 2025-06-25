package com.nefu.project.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("order_tab")
public class Order {

    @Schema(description = "订单id主键")
    @TableId(value = "order_id", type = IdType.AUTO)
    private Long orderId;

    @TableField(value = "order_uuid")
    @Schema(description = "订单雪花标识")
    private String orderUuid;

    @Schema(description = "下单用户ID")
    @TableField("order_user_uuid")
    private String orderUserUuid;

    @Schema(description = "商品ID")
    @TableField("order_product_uuid")
    private String orderProductUuid;

    @Schema(description = "购买数量")
    @TableField("order_quantity")
    private Integer orderQuantity;

    @Schema(description = "总价格")
    @TableField("order_total_price")
    private BigDecimal orderTotalPrice;

    @Schema(description = "订单状态")
    @TableField("order_status")
    private String orderStatus;

    @Schema(description = "创建时间")
    @TableField("order_created_time")
    private Date orderCreatedTime;

    @Schema(description = "更新时间")
    @TableField("order_updated_time")
    private Date orderUpdatedTime;
}