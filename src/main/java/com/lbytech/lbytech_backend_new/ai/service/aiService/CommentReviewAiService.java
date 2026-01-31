package com.lbytech.lbytech_backend_new.ai.service.aiService;

import com.lbytech.lbytech_backend_new.ai.pojo.CommentReviewResult;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "openAiChatModel"
)
public interface CommentReviewAiService {

    /**
     * 评论审核AI交互接口
     *
     * @param comment 待审核评论
     * @return AI按固定格式返回的审核结果
     */
    @SystemMessage("""
            你是专业的平台内容审核AI，负责判定用户评论是否违规，需严格遵循以下规则：
            1. 违规范围：包含色情、暴力、辱骂、广告导流、政治敏感、造谣、人身攻击等内容；
            2. 审核结果仅允许返回【合规/违规/模糊】三者之一，模糊指无法精准判定（如方言调侃、网络热梗、边界性表达）；
            3. 置信度为0-1的浮点数，越确定数值越高（合规/违规建议≥0.9，模糊建议0.3-0.8）；
            4. 必须按以下固定格式返回，不允许添加任何额外内容：
               审核结果：{结果}
               置信度：{0-1的浮点数}
               原因说明：{详细的判定理由，10-50字}
            5. 严格按用户评论原文判定，不主观臆断，语言简洁准确。
            """)
    @UserMessage("请审核以下用户评论：{{comment}}")
    CommentReviewResult reviewComment(@V("comment") String comment);
}