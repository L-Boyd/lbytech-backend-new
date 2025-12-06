package com.lbytech.lbytech_backend_new.controller;

import com.lbytech.lbytech_backend_new.pojo.vo.BaseResponse;
import com.lbytech.lbytech_backend_new.pojo.vo.NotebookVO;
import com.lbytech.lbytech_backend_new.service.INotebookService;
import com.lbytech.lbytech_backend_new.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/notebook")
public class NotebookController {

    @Autowired
    private INotebookService notebookService;

    /**
     * 上传文件
     *
     * @return
     */
    @PostMapping("/upload")
    public BaseResponse<String> fileUpload(MultipartFile file) {
        String filePath = notebookService.uploadFile(file);
        return ResultUtil.success(filePath);
    }

    /**
     * 获取文件列表
     */
    @GetMapping("/list")
    public BaseResponse<List<NotebookVO>> fileList() {

        List<NotebookVO> fileList = notebookService.getFileList();
        return ResultUtil.success(fileList);
    }

    /**
     * 根据id获取文件详情
     */
    @GetMapping("/getFileById")
    public BaseResponse<NotebookVO> getFileById(Integer id) {
        NotebookVO notebookVO = notebookService.getFileById(id);
        return ResultUtil.success(notebookVO);
    }
}
