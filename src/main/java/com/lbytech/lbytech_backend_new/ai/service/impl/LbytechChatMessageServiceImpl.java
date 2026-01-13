package com.lbytech.lbytech_backend_new.ai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbytech.lbytech_backend_new.ai.pojo.LbytechChatMessage;
import com.lbytech.lbytech_backend_new.ai.service.ILbytechChatMessageService;
import com.lbytech.lbytech_backend_new.mapper.LbytechChatMessageMapper;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LbytechChatMessageServiceImpl extends ServiceImpl<LbytechChatMessageMapper, LbytechChatMessage> implements ILbytechChatMessageService {

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        String sessionId = (String) memoryId;
        LbytechChatMessage lbytechChatMessage = this.lambdaQuery()
                .eq(LbytechChatMessage::getSessionId, sessionId)
                .one();
        if (lbytechChatMessage == null) {
            return List.of();
        }
        String content = lbytechChatMessage.getContent();

        List<ChatMessage> chatMessages = ChatMessageDeserializer.messagesFromJson(content);
        return chatMessages;
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messageList) {
        String sessionId = memoryId.toString();

        // 先删除
        this.lambdaUpdate()
                .eq(LbytechChatMessage::getSessionId, sessionId)
                .remove();

        // 再保存
        String json = ChatMessageSerializer.messagesToJson(messageList);
        LbytechChatMessage lbytechChatMessage = new LbytechChatMessage();
        lbytechChatMessage.setSessionId(sessionId);
        lbytechChatMessage.setContent(json);
        this.save(lbytechChatMessage);
    }

    @Override
    public void deleteMessages(Object memoryId) {
        String sessionId = memoryId.toString();
        this.lambdaUpdate()
                .eq(LbytechChatMessage::getSessionId, sessionId)
                .remove();
    }
}
