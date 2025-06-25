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
@TableName("knowledge_tab")
public class Knowledge {

    @Schema(description = "知识表id主键")
    @TableId(value = "knowledge_id", type = IdType.AUTO)
    private Long knowledgeId;

    @TableField(value = "knowledge_uuid")
    @Schema(description = "知识雪花标识")
    private String knowledgeUuid;

    @Schema(description = "标题")
    @TableField("knowledge_title")
    private String knowledgeTitle;

    @Schema(description = "知识内容")
    @TableField("knowledge_content")
    private String knowledgeContent;

    @Schema(description = "发布用户/专家ID")
    @TableField("knowledge_author_uuid")
    private String knowledgeAuthorUuid;

    @Schema(description = "分类")
    @TableField("knowledge_category")
    private String knowledgeCategory;

    @Schema(description = "来源")
    @TableField("knowledge_source")
    private String knowledgeSource;

    @Schema(description = "封面图片路径")
    @TableField("knowledge_cover_img")
    private String knowledgeCoverImg;

    @Schema(description = "状态")
    @TableField("knowledge_status")
    private String knowledgeStatus;

    @Schema(description = "浏览量")
    @TableField("knowledge_views")
    private Integer knowledgeViews;

    @Schema(description = "点赞数")
    @TableField("knowledge_likes")
    private Integer knowledgeLikes;

    @Schema(description = "是否置顶")
    @TableField("knowledge_is_top")
    private String knowledgeIsTop;

    @Schema(description = "创建时间")
    @TableField("knowledge_created_time")
    private Date knowledgeCreatedTime;

    @Schema(description = "更新时间")
    @TableField("knowledge_updated_time")
    private Date knowledgeUpdatedTime;
}