package com.lbytech.lbytech_backend_new.es;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

// es索引名称为notebook，自动创建索引
@Document(indexName = "notebook", createIndex = true)
@Data
@AllArgsConstructor
public class NotebookForEs {

    // ES 文档ID
    @Id
    private Integer id;

    // 字段映射：name，类型text（可分词），analyzer：分词器（ik_max_word是IK中文分词器）
    @Field(name = "file_name", type = FieldType.Text, analyzer = "ik_max_word")
    private String fileName;

    // 笔记内容
    @Field(name = "content", type = FieldType.Text, analyzer = "ik_max_word")
    private String content;

    @Field(name = "thumb_count", type = FieldType.Integer)
    private Integer thumbCount;

    @Field(name = "create_time", type = FieldType.Date, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Field(name = "update_time", type = FieldType.Date, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
