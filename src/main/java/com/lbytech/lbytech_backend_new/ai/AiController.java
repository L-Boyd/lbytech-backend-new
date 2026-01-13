package com.lbytech.lbytech_backend_new.ai;

import com.lbytech.lbytech_backend_new.ai.service.LbytechAiService;
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
    private LbytechAiService lbyTechAiService;

    @PostMapping("/testChat")
    @Operation(summary = "测试聊天", description = "测试聊天功能")   // springdoc描述方法
    public Flux<String> chat(@RequestBody AiChatRequestForm aiChatRequestForm) {
        return lbyTechAiService.testChat(aiChatRequestForm.getMessage());
    }
}
