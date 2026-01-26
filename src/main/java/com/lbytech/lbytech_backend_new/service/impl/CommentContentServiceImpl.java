package com.lbytech.lbytech_backend_new.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbytech.lbytech_backend_new.mapper.CommentContentMapper;
import com.lbytech.lbytech_backend_new.pojo.Enum.SoftDeleteStatusEnum;
import com.lbytech.lbytech_backend_new.pojo.entity.CommentContent;
import com.lbytech.lbytech_backend_new.service.ICommentContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentContentServiceImpl extends ServiceImpl<CommentContentMapper, CommentContent> implements ICommentContentService {

    @Autowired
    private CommentContentMapper commentContentMapper;

    @Override
    public void insertCommentContent(CommentContent commentContent) {
        commentContentMapper.insert(commentContent);
    }

    @Override
    public void deleteCommentContent(Long commentId) {
        this.lambdaUpdate()
                .eq(CommentContent::getId, commentId)
                .set(CommentContent::getIsDeleted, SoftDeleteStatusEnum.DELETED.getCode())
                .update();
    }
}
