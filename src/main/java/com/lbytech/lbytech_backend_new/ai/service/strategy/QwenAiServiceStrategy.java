package com.lbytech.lbytech_backend_new.ai.service.strategy;

import com.lbytech.lbytech_backend_new.ai.service.aiService.qwen.QwenOrdinaryAiService;
import com.lbytech.lbytech_backend_new.ai.service.aiService.qwen.QwenRAGAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class QwenAiServiceStrategy implements AiServiceStrategy {

    @Autowired
    private QwenOrdinaryAiService qwenOrdinaryAiService;

    @Autowired
    private QwenRAGAiService qwenRAGAiService;

    @Override
    public Flux<String> chat(String memoryId, String message) {
        return qwenOrdinaryAiService.chat(memoryId, message);
    }

    @Override
    public Flux<String> chatWithRAG(String memoryId, String message) {
        return qwenRAGAiService.chatWithRag(memoryId, message);
    }
}