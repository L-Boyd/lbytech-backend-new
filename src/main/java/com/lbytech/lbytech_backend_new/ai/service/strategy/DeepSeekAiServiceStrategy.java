package com.lbytech.lbytech_backend_new.ai.service.strategy;

import com.lbytech.lbytech_backend_new.ai.pojo.ChatModelEnum;
import com.lbytech.lbytech_backend_new.ai.service.aiService.deepseek.DeepseekOrdinaryAiService;
import com.lbytech.lbytech_backend_new.ai.service.aiService.deepseek.DeepseekRAGAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class DeepSeekAiServiceStrategy implements AiServiceStrategy {

    @Autowired
    private DeepseekOrdinaryAiService deepSeekOrdinaryAiService;

    @Autowired
    private DeepseekRAGAiService deepSeekRAGAiService;

    @Override
    public Flux<String> chat(String memoryId, String message) {
        return deepSeekOrdinaryAiService.chat(memoryId, message);
    }

    @Override
    public Flux<String> chatWithRAG(String memoryId, String message) {
        return deepSeekRAGAiService.chatWithRag(memoryId, message);
    }

    @Override
    public ChatModelEnum getModel() {
        return ChatModelEnum.DEEPSEEK;
    }
}