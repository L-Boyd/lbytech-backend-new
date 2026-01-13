package com.lbytech.lbytech_backend_new.ai.service.strategy;

import reactor.core.publisher.Flux;

/**
 * ai服务策略接口（方便选择模型）
 */
public interface AiServiceStrategy {
    Flux<String> chat(String memoryId, String message);
    Flux<String> chatWithRAG(String memoryId, String message);
}