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
        chatMemoryProvider = "chatMemoryProvider",
        contentRetriever = "contentRetriever"
)
public interface LbytechRAGAiService {

    /**
     * 有RAG知识库的流式聊天'
     *
     * @param memoryId 存储ID
     * @param message 聊天消息
     * @return 流式聊天响应
     */
    @SystemMessage("你是小博，lbytech笔记网站的AI助手。你需要先告诉用户你正在使用RAG知识库，然后再根据知识库回答用户的问题。")
    Flux<String> chatWithRag(@MemoryId String memoryId, @UserMessage String message);
}
