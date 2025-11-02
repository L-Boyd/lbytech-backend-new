package com.lbytech.lbytech_backend_new.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbytech.lbytech_backend_new.mapper.NotebookMapper;
import com.lbytech.lbytech_backend_new.pojo.entity.Notebook;
import com.lbytech.lbytech_backend_new.pojo.vo.NotebookVO;
import com.lbytech.lbytech_backend_new.service.INotebookService;
import com.lbytech.lbytech_backend_new.util.AliOssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotebookServiceImpl extends ServiceImpl<NotebookMapper, Notebook> implements INotebookService {

    @Autowired
    private AliOssUtil aliOssUtil;

    @Override
    public String uploadFile(MultipartFile file) {
        String url = aliOssUtil.uploadFile(file);
        Notebook notebook = new Notebook(file.getOriginalFilename(), url, LocalDateTime.now());
        this.save(notebook);
        return url;
    }

    @Override
    public List<NotebookVO> getFileList() {
        List<Notebook> notebookList = this.list()
                .stream()
                .distinct()
                .collect(Collectors.toList());

        List<NotebookVO> notebookVOList = notebookList.stream()
                        .map(notebook -> {
                            NotebookVO notebookVO = new NotebookVO();
                            notebookVO.setFileName(notebook.getFileName());
                            notebookVO.setFileUrl(notebook.getFileUrl());
                            return notebookVO;
                        })
                        .collect(Collectors.toList());
        return notebookVOList;
    }
}
