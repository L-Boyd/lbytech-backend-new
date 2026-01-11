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
     * 保存笔记到Elasticsearch
     *
     * @param notebookForEs 笔记实体
     */
    void save(NotebookForEs notebookForEs);

    /**
     * 更新笔记点赞数到Elasticsearch
     *
     * @param id         笔记ID
     * @param thumbCount 点赞数
     */
    void updateThumbCount(Integer id, Integer thumbCount);

    /**
     * 更新笔记内容到Elasticsearch
     *
     * @param id 笔记ID
     */
    void updateContent(Integer id);

    /**
     * 模糊查询笔记
     *
     * @param keyword 内容关键词
     * @param page    页码
     * @param size    每页数量
     * @return 分页查询结果
     */
    List<NotebookForEsVO> getByKeywordContaining(String keyword, int page, int size);
}