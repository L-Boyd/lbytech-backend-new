package com.lbytech.lbytech_backend_new.controller;

import com.lbytech.lbytech_backend_new.pojo.dto.CommentRequest;
import com.lbytech.lbytech_backend_new.pojo.vo.BaseResponse;
import com.lbytech.lbytech_backend_new.pojo.vo.CommentVO;
import com.lbytech.lbytech_backend_new.service.ICommentService;
import com.lbytech.lbytech_backend_new.util.ResultUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@Tag(name = "评论模块", description = "评论相关API")
public class CommentController {

    @Autowired
    private ICommentService commentService;

    /**
     * 发表评论
     *
     * @param commentRequest 评论请求参数
     * @return 是否成功
     */
    @PostMapping("/addComment")
    public BaseResponse<Boolean> addcomment(@RequestBody CommentRequest commentRequest) {
        commentService.addComment(commentRequest);
        return ResultUtil.success(true);
    }

    /**
     * 删除评论
     *
     * @param commentInfoId 评论信息ID
     * @return 是否成功
     */
    @DeleteMapping("/deleteComment")
    public BaseResponse<Boolean> deleteComment(@RequestParam("commentInfoId") Long commentInfoId) {
        commentService.deleteComment(commentInfoId);
        return ResultUtil.success(true);
    }


    /**
     * 分页获取笔记的所有评论
     *
     * @param notebookId 笔记ID
     * @param pageNum    页码
     * @param pageSize   每页数量
     * @return 评论列表
     */
    @GetMapping("/getCommentPageByNotebookId")
    public BaseResponse<List<CommentVO>> getCommentPageByNotebookId(@RequestParam("notebookId") Long notebookId,
                                                                    @RequestParam("pageNum") Integer pageNum,
                                                                    @RequestParam("pageSize") Integer pageSize) {
        List<CommentVO> commentVOList = commentService.getCommentPageByNotebookId(notebookId, pageNum, pageSize);
        return ResultUtil.success(commentVOList);
    }

}
