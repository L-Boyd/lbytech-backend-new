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

    // 高亮内容
    private List<String> highlightContent;

    private String fileUrl;

    private Integer thumbCount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
