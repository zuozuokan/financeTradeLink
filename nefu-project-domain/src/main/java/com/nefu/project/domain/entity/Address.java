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
@TableName("address_tab")
public class Address {

    @Schema(description = "地址id主键")
    @TableId(value = "address_id", type = IdType.AUTO)
    private Long addressId;

    @TableField(value = "address_uuid")
    @Schema(description = "地址雪花标识")
    private String addressUuid;

    @TableField(value = "address_user_uuid")
    @Schema(description = "用户ID")
    private String addressUserUuid;

    @Schema(description = "收货人姓名")
    @TableField("address_name")
    private String addressName;

    @Schema(description = "联系电话")
    @TableField("address_phone")
    private String addressPhone;

    @Schema(description = "省份")
    @TableField("address_province")
    private String addressProvince;

    @Schema(description = "城市")
    @TableField("address_city")
    private String addressCity;

    @Schema(description = "区县")
    @TableField("address_district")
    private String addressDistrict;

    @Schema(description = "详细地址")
    @TableField("address_address")
    private String addressAddress;

    @Schema(description = "是否为默认地址")
    @TableField("address_is_default")
    private String addressIsDefault;

    @Schema(description = "创建时间")
    @TableField("address_created_time")
    private Date addressCreatedTime;

    @Schema(description = "更新时间")
    @TableField("address_updated_time")
    private Date addressUpdatedTime;
}