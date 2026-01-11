package com.lbytech.lbytech_backend_new.es;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class NotebookForEsVO {

    // ES 文档ID
    @Id
    private Integer id;

    private String fileName;

    // 笔记内容
    private String content;

    private String fileUrl;

    private Integer thumbCount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    // 高亮内容
    private String highlightContent;

    // 命中段落号列表
    private List<Integer> hitParageaphNos;
}
