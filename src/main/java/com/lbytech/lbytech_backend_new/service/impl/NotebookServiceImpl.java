package com.lbytech.lbytech_backend_new.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbytech.lbytech_backend_new.mapper.NotebookMapper;
import com.lbytech.lbytech_backend_new.pojo.entity.Notebook;
import com.lbytech.lbytech_backend_new.pojo.entity.ThumbRecord;
import com.lbytech.lbytech_backend_new.pojo.vo.NotebookVO;
import com.lbytech.lbytech_backend_new.service.INotebookService;
import com.lbytech.lbytech_backend_new.service.IThumbRecordService;
import com.lbytech.lbytech_backend_new.util.AliOssUtil;
import com.lbytech.lbytech_backend_new.util.UserHolder;
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

    @Autowired
    private IThumbRecordService thumbRecordService;

    @Override
    public String uploadFile(MultipartFile file) {
        String url = aliOssUtil.uploadFile(file);
        Notebook notebook = this.lambdaQuery()
                .eq(Notebook::getFileName, file.getOriginalFilename())
                .one();
        if (notebook == null) {
            notebook = new Notebook(file.getOriginalFilename(), url, LocalDateTime.now());
            this.save(notebook);
        }
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
                            notebookVO.setId(notebook.getId());
                            notebookVO.setFileName(notebook.getFileName());
                            return notebookVO;
                        })
                        .collect(Collectors.toList());
        return notebookVOList;
    }

    @Override
    public NotebookVO getFileById(Integer id) {
        Notebook notebook = this.getById(id);
        NotebookVO notebookVO = BeanUtil.copyProperties(notebook, NotebookVO.class);

        ThumbRecord thumbRecord = thumbRecordService.lambdaQuery()
                .eq(ThumbRecord::getNotebookId, id)
                .eq(ThumbRecord::getUserEmail, UserHolder.getUser().getEmail())
                .one();
        // 查出来的记录不为空，说明用户点过赞
        notebookVO.setIsThumbed(thumbRecord != null);

        return notebookVO;
    }

}