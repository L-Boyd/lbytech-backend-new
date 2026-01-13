package com.lbytech.lbytech_backend_new.ai;

import com.lbytech.lbytech_backend_new.ai.service.LbytechOrdinaryAiService;
import com.lbytech.lbytech_backend_new.ai.service.LbytechRAGAiService;
import com.lbytech.lbytech_backend_new.pojo.vo.UserVO;
import com.lbytech.lbytech_backend_new.util.UserHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
@Tag(name = "AI模块", description = "AI相关API")    // springdoc描述类
public class AiController {

    @Autowired
    private LbytechOrdinaryAiService lbyTechOrdinaryAiService;

    @Autowired
    private LbytechRAGAiService lbyTechRAGAiService;

    @PostMapping(value = "/chat", produces = "text/html;charset=UTF-8")
    @Operation(summary = "普通聊天", description = "普通聊天功能")   // springdoc描述方法
    public Flux<String> chat(@RequestBody AiChatRequestForm aiChatRequestForm) {
        UserVO user = UserHolder.getUser();
        return lbyTechOrdinaryAiService.chat(user.getEmail(), aiChatRequestForm.getMessage());
    }

    @PostMapping(value = "/chatWithRAG", produces = "text/html;charset=UTF-8")
    @Operation(summary = "RAG聊天", description = "RAG聊天功能")   // springdoc描述方法
    public Flux<String> chatWithRAG(@RequestBody AiChatRequestForm aiChatRequestForm) {
        UserVO user = UserHolder.getUser();
        return lbyTechRAGAiService.chatWithRag(user.getEmail(), aiChatRequestForm.getMessage());
    }
}
