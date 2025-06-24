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
@TableName("expert_tab")
public class Expert {

    @Schema(description = "专家表id主键")
    @TableId(value = "expert_id", type = IdType.AUTO)
    private Long expertId;

    @TableField(value = "expert_uuid")
    @Schema(description = "专家雪花标识")
    private String expertUuid;

    @Schema(description = "关联用户ID")
    @TableField("expert_user_uuid")
    private String expertUserUuid;

    @Schema(description = "职称")
    @TableField("expert_title")
    private String expertTitle;

    @Schema(description = "头像路径")
    @TableField("expert_headshot")
    private String expertHeadshot;

    @Schema(description = "擅长领域")
    @TableField("expert_specialty")
    private String expertSpecialty;

    @Schema(description = "专家简介")
    @TableField("expert_introduction")
    private String expertIntroduction;

    @Schema(description = "资质证书路径")
    @TableField("expert_certificate")
    private String expertCertificate;

    @Schema(description = "状态")
    @TableField("expert_status")
    private String expertStatus;

    @Schema(description = "创建时间")
    @TableField("expert_created_time")
    private Date expertCreatedTime;

    @Schema(description = "更新时间")
    @TableField("expert_updated_time")
    private Date expertUpdatedTime;
}