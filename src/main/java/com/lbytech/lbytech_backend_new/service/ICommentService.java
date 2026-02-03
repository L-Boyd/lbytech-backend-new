package com.lbytech.lbytech_backend_new.service;

import com.lbytech.lbytech_backend_new.pojo.dto.CommentRequest;
import com.lbytech.lbytech_backend_new.pojo.vo.CommentVO;

import java.util.List;

public interface ICommentService {

    /**
     * 发表评论
     *
     * @param commentRequest 评论请求参数
     */
    void addComment(CommentRequest commentRequest);

    /**
     * 删除评论
     *
     * @param commentId 评论ID
     */
    void deleteComment(Long commentId);

    /**
     * 分页获取笔记的所有评论
     *
     * @param notebookId 笔记ID
     * @return 评论列表
     */
    List<CommentVO> getCommentPageByNotebookId(Long notebookId, Integer pageNum, Integer pageSize);
}