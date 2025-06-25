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
@TableName("consult_tab")
public class Consult {

    @Schema(description = "预约表id主键")
    @TableId(value = "consult_id", type = IdType.AUTO)
    private Long consultId;

    @TableField(value = "consult_uuid")
    @Schema(description = "预约雪花标识")
    private String consultUuid;

    @Schema(description = "预约用户ID")
    @TableField("consult_user_uuid")
    private String consultUserUuid;

    @Schema(description = "专家ID")
    @TableField("consult_expert_uuid")
    private String consultExpertUuid;

    @Schema(description = "类型")
    @TableField("consult_type")
    private String consultType;

    @Schema(description = "咨询标题")
    @TableField("consult_title")
    private String consultTitle;

    @Schema(description = "咨询内容")
    @TableField("consult_description")
    private String consultDescription;

    @Schema(description = "预约时间")
    @TableField("consult_appoint_time")
    private Date consultAppointTime;

    @Schema(description = "状态")
    @TableField("consult_status")
    private String consultStatus;

    @Schema(description = "创建时间")
    @TableField("consult_created_time")
    private Date consultCreatedTime;

    @Schema(description = "更新时间")
    @TableField("consult_updated_time")
    private Date consultUpdatedTime;
}