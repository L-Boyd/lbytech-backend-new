package com.lbytech.lbytech_backend_new.util;

import com.lbytech.lbytech_backend_new.es.INotebookEsService;
import com.lbytech.lbytech_backend_new.es.NotebookForEs;
import com.lbytech.lbytech_backend_new.pojo.entity.Notebook;
import com.lbytech.lbytech_backend_new.service.INotebookService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
class AliOssUtilTest {

    @Autowired
    private AliOssUtil aliOssUtil;

    @Autowired
    private INotebookService notebookService;

    @Autowired
    private INotebookEsService notebookEsService;

    /**
     * 脚本：同步所有笔记到ES
     */
    @Test
    void syncNotebookToEs() {
        List<Notebook> notebookList = notebookService.lambdaQuery().list();

        notebookList.forEach(notebook -> {
            String fileUrl = notebook.getFileUrl();
            String content = aliOssUtil.getMdFileContent(fileUrl);
            content = this.cleanMarkdownFormat(content);
            NotebookForEs notebookForEs = new NotebookForEs(notebook.getId(),
                    notebook.getFileName(),
                    content,
                    notebook.getFileUrl(),
                    notebook.getThumbCount(),
                    notebook.getCreateTime(),
                    notebook.getUpdateTime());
            // 同步到ES
            notebookEsService.saveOrUpdate(notebookForEs);
            log.info("成功同步笔记:{}到ES", notebook.getFileName());
        });
    }

    // 辅助方法：清理markdown格式符号
    private String cleanMarkdownFormat(String content) {
        if (content == null) return "";
        // 去掉标题符号、加粗符号、代码块、链接等，仅保留纯文本
        return content.replaceAll("#+\\s?", "")       // 去掉## 标题
                .replaceAll("\\*\\*(.+?)\\*\\*", "$1") // 去掉**加粗**，保留内容
                .replaceAll("`(.+?)`", "$1")    // 去掉`行内代码`，保留内容
                .replaceAll("```[\\s\\S]*?```", "") // 去掉```代码块
                .replaceAll("!\\[.*?\\]\\(.*?\\)", "") // 去掉图片
                .replaceAll("\\[(.+?)\\]\\(.*?\\)", "$1") // 去掉链接，保留文字
                .trim();
    }
}