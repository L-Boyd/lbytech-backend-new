package com.lbytech.lbytech_backend_new.ai.pojo;

import lombok.Data;

@Data
public class CommentReviewResult {

    /**
     * 审核结果："合规"、"违规"、"模糊"
     */
    String result;

    /**
     * 审核置信度，0-1之间的浮点数
     */
    Double confidence;

    /**
     * 审核原因，对审核结果的解释
     */
    String reason;
}
