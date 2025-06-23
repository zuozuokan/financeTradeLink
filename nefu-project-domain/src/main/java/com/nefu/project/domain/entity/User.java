package com.nefu.project.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("user_tab")
public class User {
    @Schema(description = "用户id主键")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "用户名")
    @TableField("user_username")
    private String userName;

    @Schema(description = "用户密码")
    @TableField("user_password")
    private String password;

    @Schema(description = "用户姓名")
    @TableField("user_name")
    private String name;

    @Schema(description = "用户生日")
    @TableField("user_birth")
    private Date birth;

    @Schema(description = "用户电话")
    @TableField("user_phone")
    private String phone;

    @Schema(description = "用户头像")
    @TableField("user_avatar")
    private String avatar;

    @Schema(description = "更新人")
    @TableField("user_update_by")
    private String updateBy;

    @Schema(description = "更新时间")
    @TableField("user_update_time")
    private Date updateTime;
}
