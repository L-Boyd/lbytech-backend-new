package com.lbytech.lbytech_backend_new.ai.service;

import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;
import reactor.core.publisher.Flux;

@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
        streamingChatModel = "openAiStreamingChatModel",
        chatMemoryProvider = "chatMemoryProvider"
)
public interface LbytechAiService {

    /**
     * 测试流式聊天
     * @param message 聊天消息
     * @return 流式聊天响应
     */
    Flux<String> testChat(String message);
}
