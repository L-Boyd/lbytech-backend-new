package com.lbytech.lbytech_backend_new.pojo.dto;

import lombok.Data;

@Data
public class CommentRequest {

    /**
     * 评论的笔记ID
     */
    Integer notebookId;

     /**
     * 父评论ID，0表示一级评论
     */
    Long parentId;

    /**
     * 评论内容
     */
    String content;
}
