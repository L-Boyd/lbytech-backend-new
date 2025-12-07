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

     /**
     * 取消点赞笔记
     *
     * @param notebookId 要取消点赞的笔记id
     * @return 是否成功
     */
    Boolean unThumbNotebook(Integer notebookId);

     /**
     * 查询用户是否点赞了该笔记
     *
     * @param userEmail 用户邮箱
     * @param notebookId 笔记id
     * @return 是否点赞了该笔记
     */
    Boolean hasThumb(String userEmail, Integer notebookId);
}
