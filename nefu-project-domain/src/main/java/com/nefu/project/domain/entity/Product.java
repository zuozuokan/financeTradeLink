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
@TableName("product_tab")
public class Product {

    @Schema(description = "商品id主键")
    @TableId(value = "product_id", type = IdType.AUTO)
    private Long productId;

    @TableField(value = "product_uuid")
    @Schema(description = "商品雪花标识")
    private String productUuid;

    @Schema(description = "商品名称")
    @TableField("product_name")
    private String productName;

    @Schema(description = "商品分类")
    @TableField("product_category")
    private String productCategory;

    @Schema(description = "商品描述")
    @TableField("product_description")
    private String productDescription;

    @Schema(description = "商品单价")
    @TableField("product_price")
    private BigDecimal productPrice;

    @Schema(description = "库存数量")
    @TableField("product_stock")
    private Integer productStock;

    @Schema(description = "商品图片地址")
    @TableField("product_image_url")
    private String productImageUrl;

    @Schema(description = "发布用户的ID")
    @TableField("product_user_uuid")
    private String productUserUuid;
    @Schema(description = "商品状态")
    @TableField("product_status")
    private String productStatus;

    @Schema(description = "创建时间")
    @TableField("product_created_time")
    private Date productCreatedTime;

    @Schema(description = "更新时间")
    @TableField("product_updated_time")
    private Date productUpdatedTime;
}