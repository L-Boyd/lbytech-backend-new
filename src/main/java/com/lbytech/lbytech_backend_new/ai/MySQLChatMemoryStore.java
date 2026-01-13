package com.lbytech.lbytech_backend_new.ai;

import com.lbytech.lbytech_backend_new.ai.service.ILbytechChatMessageService;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MySQLChatMemoryStore implements ChatMemoryStore {

    @Autowired
    private ILbytechChatMessageService lbytechChatMessageService;

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        return lbytechChatMessageService.getMessages(memoryId);
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messageList) {
        lbytechChatMessageService.updateMessages(memoryId, messageList);
    }

    @Override
    public void deleteMessages(Object memoryId) {
        lbytechChatMessageService.deleteMessages(memoryId);
    }
}
