package com.lbytech.lbytech_backend_new.ai;

import com.lbytech.lbytech_backend_new.ai.properties.DeepseekProperties;
import com.lbytech.lbytech_backend_new.ai.properties.LbytechEmbeddingStoreProperties;
import com.lbytech.lbytech_backend_new.ai.properties.QwenProperties;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Autowired
    private MySQLChatMemoryStore mySQLChatMemoryStore;

    @Autowired
    private LbytechEmbeddingStoreProperties lbytechEmbeddingStoreProperties;

    @Autowired
    private EmbeddingModel embeddingModel;

    @Bean
    // 构建ChatMemoryProvider对象，前端传memoryId，langchain4j根据memoryId匹配话记忆对象，没匹配到就会用get方法来获取chatMemory
    public ChatMemoryProvider chatMemoryProvider() {
        ChatMemoryProvider chatMemoryProvider = new ChatMemoryProvider() {
            @Override
            public ChatMemory get(Object memoryId) {
                return MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .maxMessages(20)
                        .chatMemoryStore(mySQLChatMemoryStore)
                        .build();
            }
        };
        return chatMemoryProvider;
    }

    /**
     * 构建向量数据库操作对象
     *
     * @return embeddingStore
     */
    @Bean
    public EmbeddingStore embeddingStore() {

        // 构建向量数据库操作对象
        EmbeddingStore embeddingStore = PgVectorEmbeddingStore.builder()
                .host(lbytechEmbeddingStoreProperties.getHost())
                .port(lbytechEmbeddingStoreProperties.getPort())
                .database(lbytechEmbeddingStoreProperties.getDatabase())
                .user(lbytechEmbeddingStoreProperties.getUsername())
                .password(lbytechEmbeddingStoreProperties.getPassword())
                .table(lbytechEmbeddingStoreProperties.getTable())
                .dimension(embeddingModel.dimension()) // 向量维度
                .build();

        return embeddingStore;
    }

    // 构建向量数据库检索对象
    @Bean
    public ContentRetriever contentRetriever(EmbeddingStore embeddingStore) {   // spring里要用IOC容器内的对象可以直接声明
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .minScore(0.5)  // 最小的可选入的预选相似度值
                .maxResults(3)  // 最多可查询出的片段
                .embeddingModel(embeddingModel)
                .build();
    }

    // DeepSeek模型配置
    @Bean
    public OpenAiStreamingChatModel deepSeekChatModel(DeepseekProperties deepseekProperties) {
        return dev.langchain4j.model.openai.OpenAiStreamingChatModel.builder()
                .baseUrl(deepseekProperties.getBaseUrl())
                .apiKey(deepseekProperties.getApiKey())
                .modelName(deepseekProperties.getModelName())
                .maxTokens(deepseekProperties.getMaxTokens())
                .logRequests(deepseekProperties.isLogRequests())
                .logResponses(deepseekProperties.isLogResponses())
                .build();
    }

    // Qwen模型配置
    @Bean("qwenChatModel")
    public OpenAiStreamingChatModel qwenChatModel(QwenProperties qwenProperties) {
        return dev.langchain4j.model.openai.OpenAiStreamingChatModel.builder()
                .baseUrl(qwenProperties.getBaseUrl())
                .apiKey(qwenProperties.getApiKey())
                .modelName(qwenProperties.getModelName())
                .maxTokens(qwenProperties.getMaxTokens())
                .logRequests(qwenProperties.isLogRequests())
                .logResponses(qwenProperties.isLogResponses())
                .build();
    }
}
