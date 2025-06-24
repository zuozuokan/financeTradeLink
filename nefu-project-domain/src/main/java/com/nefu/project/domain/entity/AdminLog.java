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
@TableName("admin_log_tab")
public class AdminLog {

    @Schema(description = "日志表id主键")
    @TableId(value = "admin_log_id", type = IdType.AUTO)
    private Long adminLogId;

    @TableField(value = "admin_log_uuid")
    @Schema(description = "日志雪花标识")
    private String adminLogUuid;

    @Schema(description = "操作管理员ID")
    @TableField("admin_log_admin_uuid")
    private String adminLogAdminUuid;

    @Schema(description = "操作目标ID")
    @TableField("admin_log_target_uuid")
    private String adminLogTargetUuid;

    @Schema(description = "目标类型")
    @TableField("admin_log_target_type")
    private String adminLogTargetType;

    @Schema(description = "操作类型")
    @TableField("admin_log_action")
    private String adminLogAction;

    @Schema(description = "操作描述")
    @TableField("admin_log_description")
    private String adminLogDescription;

    @Schema(description = "创建时间")
    @TableField("admin_log_created_time")
    private Date adminLogCreatedTime;

    @Schema(description = "更新时间")
    @TableField("admin_log_updated_time")
    private Date adminLogUpdatedTime;
}