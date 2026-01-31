package com.lbytech.lbytech_backend_new.ai.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("comment_review_result")
@Data
public class CommentReviewResultForDB {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关联的评论ID
     */
    private Long commentId;

    /**
     * 审核结果："合规"、"违规"、"模糊"
     */
    private String result;

    /**
     * 审核置信度，0-1之间的浮点数
     */
    private Double confidence;

    /**
     * 审核原因，对审核结果的解释
     */
    private String reason;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
