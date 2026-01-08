package com.lbytech.lbytech_backend_new.es;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

// 泛型：<实体类, ID类型>
public interface NotebookEsRepository extends ElasticsearchRepository<NotebookForEs, Integer> {
    // 自定义查询方法（JPA 命名规则，自动生成查询语句）
    // 根据文件名模糊查询笔记（Containing 对应 ES 的模糊匹配）
    List<NotebookForEs> findByFileNameContaining(String fileName);

    // 根据内容模糊查询
    Page<NotebookForEs> findByContentContaining(String content, Pageable pageable);
}