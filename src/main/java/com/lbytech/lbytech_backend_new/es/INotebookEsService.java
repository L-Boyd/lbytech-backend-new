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
     * 模糊查询笔记
     * @param keyword 内容关键词
     * @param page 页码
     * @param size 每页数量
     * @return 分页查询结果
     */
    List<NotebookForEsVO> getByKeywordContaining(String keyword, int page, int size);
}