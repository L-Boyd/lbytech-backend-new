package com.lbytech.lbytech_backend_new.ai;

import com.lbytech.lbytech_backend_new.ai.pojo.ChatModelEnum;
import lombok.Data;

@Data
public class AiChatRequestForm {

    private ChatModelEnum model;
    private String message;
}
