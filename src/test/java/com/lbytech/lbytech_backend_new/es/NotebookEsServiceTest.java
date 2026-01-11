package com.lbytech.lbytech_backend_new.es;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NotebookEsServiceTest {

    @Autowired
    private INotebookEsService notebookEsService;


    /**
     * 创建索引脚本
     */
    @Test
    void testCreateIndex() {
        notebookEsService.createIndex();
    }
}