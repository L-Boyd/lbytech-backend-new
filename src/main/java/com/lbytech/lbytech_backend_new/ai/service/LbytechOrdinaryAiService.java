package com.lbytech.lbytech_backend_new.ai.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;
import reactor.core.publisher.Flux;

@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
        streamingChatModel = "openAiStreamingChatModel",
        chatMemoryProvider = "chatMemoryProvider"
)
public interface LbytechOrdinaryAiService {

    /**
     * 普通流式聊天
     * @param message 聊天消息
     * @return 流式聊天响应
     */
    @SystemMessage("你是小博，lbytech笔记网站的AI助手。你需要根据用户的问题，回答用户的问题（但是不要回答跟计算机无关的问题）。")
    Flux<String> chat(@MemoryId String memoryId, @UserMessage String message);
}
