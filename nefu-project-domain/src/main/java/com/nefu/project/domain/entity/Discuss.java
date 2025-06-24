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
@TableName("discuss_tab")
public class Discuss {

    @Schema(description = "讨论表id主键")
    @TableId(value = "discuss_id", type = IdType.AUTO)
    private Long discussId;

    @TableField(value = "discuss_uuid")
    @Schema(description = "讨论雪花标识")
    private String discussUuid;

    @Schema(description = "关联知识ID")
    @TableField("discuss_knowledge_uuid")
    private String discussKnowledgeUuid;

    @Schema(description = "评论用户ID")
    @TableField("discuss_user_uuid")
    private String discussUserUuid;

    @Schema(description = "评论内容")
    @TableField("discuss_content")
    private String discussContent;

    @Schema(description = "父评论ID")
    @TableField("discuss_parent_uuid")
    private String discussParentUuid;

    @Schema(description = "状态")
    @TableField("discuss_status")
    private String discussStatus;

    @Schema(description = "点赞数")
    @TableField("discuss_likes")
    private Integer discussLikes;

    @Schema(description = "创建时间")
    @TableField("discuss_created_time")
    private Date discussCreatedTime;

    @Schema(description = "更新时间")
    @TableField("discuss_updated_time")
    private Date discussUpdatedTime;
}