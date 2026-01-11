package com.lbytech.lbytech_backend_new.es;

import java.util.List;

/**
 * Elasticsearch服务接口
 */
public interface INotebookEsService {

    /**
     * 创建索引
     */
    void createIndex();


    /**
     * 保存或更新笔记到Elasticsearch
     * @param notebookForEs 笔记实体
     */
    void saveOrUpdate(NotebookForEs notebookForEs);

    /**
     * 根据文件名模糊查询笔记
     * @param fileName 文件名关键词
     * @return 笔记列表
     */
    List<NotebookForEsVO> getNotebookByFileNameContaining(String fileName);
}