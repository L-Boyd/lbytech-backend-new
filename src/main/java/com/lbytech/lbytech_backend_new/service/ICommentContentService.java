package com.lbytech.lbytech_backend_new.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lbytech.lbytech_backend_new.pojo.entity.CommentContent;

public interface ICommentContentService extends IService<CommentContent> {

    /**
     * 插入评论内容
     *
     * @param commentContent 评论内容
     */
    void insertCommentContent(CommentContent commentContent);

    /**
     * 删除评论内容
     *
     * @param commentId 评论ID
     */
    void deleteCommentContent(Long commentId);
}
