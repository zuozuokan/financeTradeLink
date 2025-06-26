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

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("cart_tab")
public class Cart {

    @Schema(description = "购物车id主键")
    @TableId(value = "cart_id", type = IdType.AUTO)
    private Long cartId;

    @TableField(value = "cart_uuid")
    @Schema(description = "购物车雪花标识")
    private String cartUuid;

    @Schema(description = "用户ID")
    @TableField("cart_user_uuid")
    private String cartUserUuid;

    @Schema(description = "商品ID")
    @TableField("cart_product_uuid")
    private String cartProductUuid;

    @Schema(description = "商品数量")
    @TableField("cart_quantity")
    private int cartQuantity;

    @Schema(description = "创建时间")
    @TableField("cart_created_time")
    private Date cartCreatedTime;

    @Schema(description = "更新时间")
    @TableField("cart_updated_time")
    private Date cartUpdatedTime;
}