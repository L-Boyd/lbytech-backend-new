package com.lbytech.lbytech_backend_new.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbytech.lbytech_backend_new.ai.pojo.CommentReviewResultForDB;
import com.lbytech.lbytech_backend_new.service.ICommentReviewResultService;
import com.lbytech.lbytech_backend_new.mapper.CommentReviewResultMapper;
import org.springframework.stereotype.Service;

/**
 * @author 15082
 * @description 针对表【comment_review_result(评论审核结果表)】的数据库操作Service实现
 * @createDate 2026-01-31 13:30:06
 */
@Service
public class CommentReviewResultServiceImpl extends ServiceImpl<CommentReviewResultMapper, CommentReviewResultForDB>
        implements ICommentReviewResultService {

}