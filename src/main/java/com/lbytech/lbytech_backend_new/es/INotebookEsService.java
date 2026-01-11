package com.lbytech.lbytech_backend_new.es;;

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
}