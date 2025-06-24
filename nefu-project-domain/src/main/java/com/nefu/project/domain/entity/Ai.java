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
@TableName("ai_tab")
public class Ai {

    @Schema(description = "问答表id主键")
    @TableId(value = "ai_id", type = IdType.AUTO)
    private Long aiId;

    @TableField(value = "ai_uuid")
    @Schema(description = "问答雪花标识")
    private String aiUuid;

    @Schema(description = "用户ID")
    @TableField("ai_user_uuid")
    private String aiUserUuid;

    @Schema(description = "用户问题")
    @TableField("ai_question")
    private String aiQuestion;

    @Schema(description = "AI回答")
    @TableField("ai_answer")
    private String aiAnswer;

    @Schema(description = "是否有用")
    @TableField("ai_is_helpful")
    private String aiIsHelpful;

    @Schema(description = "用户反馈")
    @TableField("ai_feedback")
    private String aiFeedback;

    @Schema(description = "创建时间")
    @TableField("ai_created_time")
    private Date aiCreatedTime;

    @Schema(description = "更新时间")
    @TableField("ai_updated_time")
    private Date aiUpdatedTime;
}