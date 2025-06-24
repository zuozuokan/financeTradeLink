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
@TableName("demand_tab")
public class Demand {

    @Schema(description = "需求id主键")
    @TableId(value = "demand_id", type = IdType.AUTO)
    private Long demandId;

    @TableField(value = "demand_uuid")
    @Schema(description = "需求雪花标识")
    private String demandUuid;

    @Schema(description = "需求用户ID")
    @TableField("demand_user_uuid")
    private String demandUserUuid;

    @Schema(description = "需求名称")
    @TableField("demand_name")
    private String demandName;

    @Schema(description = "需求分类")
    @TableField("demand_category")
    private String demandCategory;

    @Schema(description = "需求描述")
    @TableField("demand_description")
    private String demandDescription;

    @Schema(description = "需求数量")
    @TableField("demand_quantity")
    private Integer demandQuantity;

    @Schema(description = "预算")
    @TableField("demand_budget")
    private BigDecimal demandBudget;

    @Schema(description = "需求状态")
    @TableField("demand_status")
    private String demandStatus;

    @Schema(description = "创建时间")
    @TableField("demand_created_time")
    private Date demandCreatedTime;

    @Schema(description = "更新时间")
    @TableField("demand_updated_time")
    private Date demandUpdatedTime;
}