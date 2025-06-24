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
@TableName("loan_application_tab")
public class LoanApplication {

    @Schema(description = "融资表id主键")
    @TableId(value = "loan_application_id", type = IdType.AUTO)
    private Long loanApplicationId;

    @TableField(value = "loan_application_uuid")
    @Schema(description = "融资雪花标识")
    private String loanApplicationUuid;

    @Schema(description = "申请人ID")
    @TableField("loan_application_user_uuid")
    private String loanApplicationUserUuid;

    @Schema(description = "融资金额")
    @TableField("loan_application_amount")
    private BigDecimal loanApplicationAmount;

    @Schema(description = "融资用途")
    @TableField("loan_application_purpose")
    private String loanApplicationPurpose;

    @Schema(description = "还款计划")
    @TableField("loan_application_repay_plan")
    private String loanApplicationRepayPlan;

    @Schema(description = "申请状态")
    @TableField("loan_application_status")
    private String loanApplicationStatus;

    @Schema(description = "处理银行人员ID")
    @TableField("loan_application_banker_uuid")
    private String loanApplicationBankerUuid;

    @Schema(description = "创建时间")
    @TableField("loan_application_created_time")
    private Date loanApplicationCreatedTime;

    @Schema(description = "更新时间")
    @TableField("loan_application_updated_time")
    private Date loanApplicationUpdatedTime;
}