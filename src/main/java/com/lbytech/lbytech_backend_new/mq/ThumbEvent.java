package com.lbytech.lbytech_backend_new.mq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 点赞事件
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThumbEvent implements Serializable {

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 笔记ID
     */
    private Integer notebookId;

    /**
     * 事件类型
     */
    private EventType type;

    /**
     * 事件发生时间
     */
    private LocalDateTime eventTime;

    /**
     * 事件类型枚举
     */
    public enum EventType {
        /**
         * 点赞
         */
        INCR,

        /**
         * 取消点赞
         */
        DECR
    }
}
