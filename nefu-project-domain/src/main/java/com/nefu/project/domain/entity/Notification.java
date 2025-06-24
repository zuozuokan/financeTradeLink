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
@TableName("notification_tab")
public class Notification {

    @Schema(description = "通知表id主键")
    @TableId(value = "notification_id", type = IdType.AUTO)
    private Long notificationId;

    @TableField(value = "notification_uuid")
    @Schema(description = "通知雪花标识")
    private String notificationUuid;

    @Schema(description = "接收用户ID")
    @TableField("notification_recipient_uuid")
    private String notificationRecipientUuid;

    @Schema(description = "发送者ID")
    @TableField("notification_sender_uuid")
    private String notificationSenderUuid;

    @Schema(description = "通知类型")
    @TableField("notification_type")
    private String notificationType;

    @Schema(description = "通知标题")
    @TableField("notification_title")
    private String notificationTitle;

    @Schema(description = "通知内容")
    @TableField("notification_content")
    private String notificationContent;

    @Schema(description = "状态")
    @TableField("notification_status")
    private String notificationStatus;

    @Schema(description = "创建时间")
    @TableField("notification_created_time")
    private Date notificationCreatedTime;

    @Schema(description = "更新时间")
    @TableField("notification_updated_time")
    private Date notificationUpdatedTime;
}