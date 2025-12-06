package com.lbytech.lbytech_backend_new.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lbytech.lbytech_backend_new.pojo.entity.ThumbRecord;

public interface IThumbRecordService extends IService<ThumbRecord> {

    /**
     * 点赞笔记
     *
     * @param notebookId 要点赞的笔记id
     * @return 是否成功
     */
    Boolean thumbNotebook(Integer notebookId);

}
