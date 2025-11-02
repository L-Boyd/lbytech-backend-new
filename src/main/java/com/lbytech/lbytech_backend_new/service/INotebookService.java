package com.lbytech.lbytech_backend_new.service;

import com.lbytech.lbytech_backend_new.pojo.vo.NotebookVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface INotebookService {

    /**
     * 上传文件
     * @param file 文件
     * @return 上传后的文件路径
     */
    String uploadFile(MultipartFile file);

    /**
     * 获取文件列表
     *
     * @return 文件列表
     */
    List<NotebookVO> getFileList();

}
