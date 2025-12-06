package com.lbytech.lbytech_backend_new.controller;

import com.lbytech.lbytech_backend_new.pojo.vo.BaseResponse;
import com.lbytech.lbytech_backend_new.service.IThumbRecordService;
import com.lbytech.lbytech_backend_new.util.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/thumb")
@Tag(name = "点赞模块", description = "点赞相关API")    // springdoc描述类
public class ThumbController {

    @Autowired
    private IThumbRecordService thumbRecordService;

    @PostMapping("/thumbNotebook")
    @Operation(summary = "点赞笔记本")   // springdoc描述方法
    public BaseResponse<Boolean> thumbNotebook(Integer notebookId) {
        Boolean success = thumbRecordService.thumbNotebook(notebookId);
        return ResultUtil.success(success);
    }

    @PostMapping("/unThumbNotebook")
    @Operation(summary = "取消点赞笔记本")   // springdoc描述方法
    public BaseResponse<Boolean> unThumbNotebook(Integer notebookId) {
        Boolean success = thumbRecordService.unThumbNotebook(notebookId);
        return ResultUtil.success(success);
    }

}
