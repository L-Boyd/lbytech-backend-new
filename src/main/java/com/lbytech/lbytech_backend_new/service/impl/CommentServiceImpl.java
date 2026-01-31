package com.lbytech.lbytech_backend_new.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbytech.lbytech_backend_new.ai.pojo.CommentReviewResult;
import com.lbytech.lbytech_backend_new.ai.pojo.CommentReviewResultForDB;
import com.lbytech.lbytech_backend_new.ai.service.aiService.CommentReviewAiService;
import com.lbytech.lbytech_backend_new.exception.BusinessException;
import com.lbytech.lbytech_backend_new.mapper.CommentInfoMapper;
import com.lbytech.lbytech_backend_new.pojo.Enum.CommentStatusEnum;
import com.lbytech.lbytech_backend_new.pojo.Enum.SoftDeleteStatusEnum;
import com.lbytech.lbytech_backend_new.pojo.Enum.StatusCodeEnum;
import com.lbytech.lbytech_backend_new.pojo.dto.CommentRequest;
import com.lbytech.lbytech_backend_new.pojo.entity.CommentContent;
import com.lbytech.lbytech_backend_new.pojo.entity.CommentInfo;
import com.lbytech.lbytech_backend_new.pojo.vo.UserVO;
import com.lbytech.lbytech_backend_new.service.*;
import com.lbytech.lbytech_backend_new.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentInfoMapper, CommentInfo> implements ICommentService {

    @Autowired
    private ICommentInfoService commentInfoService;

    @Autowired
    private ICommentContentService commentContentService;

    @Autowired
    private CommentReviewAiService commentReviewAiService;

    @Autowired
    private ICommentReviewResultService commentReviewResultService;

    @Autowired
    private IMailService mailService;

    @Override
    @Transactional
    public void addComment(CommentRequest commentRequest) {
        // 获取当前用户
        UserVO currentUser = UserHolder.getUser();
        if (currentUser == null) {
            throw new BusinessException(StatusCodeEnum.FAIL, "用户未登录");
        }

        // 创建评论信息
        CommentInfo commentInfo = new CommentInfo();
        commentInfo.setUserEmail(currentUser.getEmail());
        commentInfo.setNotebookId(commentRequest.getNotebookId());
        commentInfo.setParentId(commentRequest.getParentId());
        commentInfo.setLikeCount(0);
        commentInfo.setStatus(CommentStatusEnum.PENDING.getCode());
        commentInfo.setCreateTime(LocalDateTime.now());
        commentInfo.setIsDeleted(SoftDeleteStatusEnum.NOT_DELETED.getCode());

        // 插入评论信息
        commentInfoService.insertCommentInfo(commentInfo);

        // 创建评论内容
        Long commentId = commentInfo.getId();
        CommentContent commentContent = new CommentContent();
        commentContent.setCommentId(commentId);
        commentContent.setContent(commentRequest.getContent());
        commentContent.setCreateTime(LocalDateTime.now());
        commentContent.setIsDeleted(SoftDeleteStatusEnum.NOT_DELETED.getCode()); // 未删除

        // 插入评论内容
        commentContentService.insertCommentContent(commentContent);

        // 评论审核
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        Thread.startVirtualThread(() -> reviewComment(commentContent.getId()));
                    }
                }
        );
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        CommentInfo commentInfo = commentInfoService.getById(commentId);
        UserVO currentUser = UserHolder.getUser();
        if (!currentUser.getEmail().equals(commentInfo.getUserEmail())) {
            throw new BusinessException(StatusCodeEnum.UNAUTHORIZED, "用户权限不足");
        }

        // 逻辑删除评论信息
        commentInfoService.deleteCommentInfo(commentId);

        // 逻辑删除评论内容
        commentContentService.deleteCommentContent(commentId);
    }

    @Override
    public List<CommentInfo> getCommentsByNotebookId(Long notebookId, Integer pageNum, Integer pageSize) {
        // TODO 分页查询评论
        return List.of();
    }

    @Transactional
    public void reviewComment(Long commentContentId) {
        String commentContent = commentContentService.getById(commentContentId).getContent();
        CommentReviewResult commentReviewResult = commentReviewAiService.reviewComment(commentContent);
        CommentReviewResultForDB commentReviewResultForDB = BeanUtil.copyProperties(commentReviewResult, CommentReviewResultForDB.class);
        commentReviewResultForDB.setCommentId(commentContentId);

        commentReviewResultService.save(commentReviewResultForDB);

        CommentInfo commentInfo = commentInfoService.getById(commentContentId);
        if (commentReviewResult.getResult().equals("合规")) {
            commentInfo.setStatus(CommentStatusEnum.APPROVED.getCode());
            commentInfoService.updateById(commentInfo);
        } else if (commentReviewResult.getResult().equals("违规")) {
            commentInfo.setStatus(CommentStatusEnum.REJECTED.getCode());
            commentInfoService.updateById(commentInfo);
        } else if (commentReviewResult.getResult().equals("模糊")) {
            commentInfo.setStatus(CommentStatusEnum.AMBIGUOUS.getCode());
            commentInfoService.updateById(commentInfo);

            // 通知人工审核
            mailService.sendSimpleMail("1508201988@qq.com", "评论需要人工审核", "评论id为" + commentInfo.getId());
        }
    }
}