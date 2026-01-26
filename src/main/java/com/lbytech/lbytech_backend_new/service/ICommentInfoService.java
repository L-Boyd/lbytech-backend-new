package com.lbytech.lbytech_backend_new.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lbytech.lbytech_backend_new.pojo.entity.CommentInfo;

public interface ICommentInfoService extends IService<CommentInfo> {

    /**
     * 插入评论信息
     *
     * @param commentInfo 评论信息
     */
    void insertCommentInfo(CommentInfo commentInfo);

    /**
     * 删除评论信息
     *
     * @param commentId 评论ID
     */
    void deleteCommentInfo(Long commentId);
}
