package com.lbytech.lbytech_backend_new.ai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lbytech.lbytech_backend_new.ai.pojo.LbytechChatMessage;
import dev.langchain4j.data.message.ChatMessage;

import java.util.List;

public interface ILbytechChatMessageService extends IService<LbytechChatMessage> {

    List<ChatMessage> getMessages(Object memoryId);

    void updateMessages(Object memoryId, List<ChatMessage> messageList);

    void deleteMessages(Object memoryId);
}
