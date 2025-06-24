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
@TableName("investment_record_tab")
public class InvestmentRecord {

    @Schema(description = "投资表id主键")
    @TableId(value = "investment_record_id", type = IdType.AUTO)
    private Long investmentRecordId;

    @TableField(value = "investment_record_uuid")
    @Schema(description = "投资雪花标识")
    private String investmentRecordUuid;

    @Schema(description = "投资人ID")
    @TableField("investment_record_investor_uuid")
    private String investmentRecordInvestorUuid;

    @Schema(description = "关联融资项目ID")
    @TableField("investment_record_loan_uuid")
    private String investmentRecordLoanUuid;

    @Schema(description = "创建时间")
    @TableField("investment_record_created_time")
    private Date investmentRecordCreatedTime;

    @Schema(description = "更新时间")
    @TableField("investment_record_updated_time")
    private Date investmentRecordUpdatedTime;
}