package com.lbytech.lbytech_backend_new.ai.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import com.baomidou.mybatisplus.annotation.TableName;

@Data
@TableName("chat_memory")
public class LbytechChatMessage {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String sessionId;

    // 聊天记录(json格式)
    private String content;
}
