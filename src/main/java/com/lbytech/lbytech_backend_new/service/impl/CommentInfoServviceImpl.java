package com.lbytech.lbytech_backend_new.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbytech.lbytech_backend_new.mapper.CommentInfoMapper;
import com.lbytech.lbytech_backend_new.pojo.Enum.CommentStatusEnum;
import com.lbytech.lbytech_backend_new.pojo.Enum.SoftDeleteStatusEnum;
import com.lbytech.lbytech_backend_new.pojo.entity.CommentInfo;
import com.lbytech.lbytech_backend_new.service.ICommentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentInfoServviceImpl extends ServiceImpl<CommentInfoMapper, CommentInfo> implements ICommentInfoService {

    @Autowired
    private CommentInfoMapper commentInfoMapper;

    @Override
    public void insertCommentInfo(CommentInfo commentInfo) {
        commentInfoMapper.insert(commentInfo);
    }

    @Override
    public void deleteCommentInfo(Long commentId) {
        this.lambdaUpdate()
                .eq(CommentInfo::getId, commentId)
                .set(CommentInfo::getIsDeleted, SoftDeleteStatusEnum.DELETED.getCode())
                .set(CommentInfo::getStatus, CommentStatusEnum.DISABLED.getCode())
                .update();
    }
}
