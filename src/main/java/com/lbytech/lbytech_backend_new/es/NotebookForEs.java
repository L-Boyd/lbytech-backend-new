package com.lbytech.lbytech_backend_new.es;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotebookForEs {

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
}
