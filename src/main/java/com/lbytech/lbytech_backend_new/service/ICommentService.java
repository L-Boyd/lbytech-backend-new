package com.lbytech.lbytech_backend_new.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lbytech.lbytech_backend_new.pojo.entity.CommentInfo;

import java.util.List;

public interface ICommentService extends IService<CommentInfo> {

    /**
     * 发表评论
     *
     * @param notebookId 评论请求参数
     * @param content 评论内容
     */
    void addComment(Integer notebookId, String content);

    /**
     * 删除评论
     *
     * @param commentId 评论ID
     */
    void deleteComment(Long commentId);

    /**
     * 更新评论内容
     *
     * @param commentId 评论ID
     * @param content   新内容
     */
    void updateComment(Long commentId, String content);

    /**
     * 获取笔记的所有评论
     *
     * @param notebookId 笔记ID
     * @return 评论列表
     */
    List<CommentInfo> getCommentsByNotebookId(Long notebookId);
}