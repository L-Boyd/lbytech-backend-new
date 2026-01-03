package com.lbytech.lbytech_backend_new.controller;


import com.lbytech.lbytech_backend_new.pojo.dto.ThumbRequest;
import com.lbytech.lbytech_backend_new.pojo.vo.BaseResponse;
import com.lbytech.lbytech_backend_new.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @GetMapping("/hello")
    public BaseResponse<String> thumbNotebook() {
        log.info("hello");
        return ResultUtil.success("hello");
    }
}
