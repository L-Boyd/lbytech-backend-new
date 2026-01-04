package com.lbytech.lbytech_backend_new.controller;

import com.lbytech.lbytech_backend_new.pojo.dto.ThumbRequest;
import com.lbytech.lbytech_backend_new.pojo.vo.BaseResponse;
import com.lbytech.lbytech_backend_new.service.IThumbRecordService;
import com.lbytech.lbytech_backend_new.util.ResultUtil;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/thumb")
@Tag(name = "点赞模块", description = "点赞相关API")    // springdoc描述类
public class ThumbController {

    @Autowired
    private IThumbRecordService thumbRecordService;

    // 计数器：记录点赞成功和失败的次数
    private final Counter successCounter;
    private final Counter failureCounter;
    public ThumbController(MeterRegistry meterRegistry) {
        this.successCounter = Counter.builder("thumb.success.count")
                .description("Total successful thumb")
                .register(meterRegistry);
        this.failureCounter = Counter.builder("thumb.failure.count")
                .description("Total failed thumb")
                .register(meterRegistry);
    }

    @PostMapping("/thumbNotebook")
    @Operation(summary = "点赞笔记本")   // springdoc描述方法
    public BaseResponse<Boolean> thumbNotebook(@RequestBody ThumbRequest thumbRequest) {
        try {
            Boolean success = thumbRecordService.thumbNotebook(thumbRequest.getNotebookId());
            if (success) {
                // 成功计数
                successCounter.increment();
                return ResultUtil.success(true);
            } else {
                // 失败计数
                failureCounter.increment();
                return ResultUtil.fail(false);
            }
        } catch (Exception e) {
            // 失败计数
            failureCounter.increment();
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/unThumbNotebook")
    @Operation(summary = "取消点赞笔记本")   // springdoc描述方法
    public BaseResponse<Boolean> unThumbNotebook(@RequestBody ThumbRequest thumbRequest) {
        Boolean success = thumbRecordService.unThumbNotebook(thumbRequest.getNotebookId());
        return ResultUtil.success(success);
    }

}
