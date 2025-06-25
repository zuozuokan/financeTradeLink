package com.nefu.project.domain.entity;

import com.alibaba.druid.filter.AutoLoad;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("user_tab")
public class User {

    @Schema(description = "用户id主键")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @TableField("user_uuid")
    @Schema(description = "用户雪花标识")
    private String userUuid;

    @Schema(description = "用户名")
    @TableField("user_username")
    private String userUserName;

    @Schema(description = "用户密码")
    @TableField("user_password")
    private String userPassword;

    @Schema(description = "用户余额")
    @TableField("user_amount")
    private BigDecimal userAmount;

    @Schema(description = "用户角色")
    @TableField("user_role")
    private String userRole;

    @Schema(description = "用户姓名")
    @TableField("user_name")
    private String userName;

    @Schema(description = "用户电话")
    @TableField("user_phone")
    private String userPhone;

    @Schema(description = "用户状态")
    @TableField("user_status")
    private String userStatus;

    @Schema(description = "用户创建时间")
    @TableField("user_created_time")
    private Date userCreateTime;

    @Schema(description = "用户更新时间")
    @TableField("user_updated_time")
    private Date userUpdateTime;

}