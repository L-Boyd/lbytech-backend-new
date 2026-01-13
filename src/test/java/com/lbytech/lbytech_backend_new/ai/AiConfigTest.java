package com.lbytech.lbytech_backend_new.ai;

import com.lbytech.lbytech_backend_new.pojo.entity.Notebook;
import com.lbytech.lbytech_backend_new.service.INotebookService;
import com.lbytech.lbytech_backend_new.util.AliOssUtil;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@SpringBootTest
class AiConfigTest {

    @Autowired
    private INotebookService notebookService;

    @Autowired
    private EmbeddingStore embeddingStore;

    @Autowired
    private EmbeddingModel embeddingModel;

    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 脚本：存储所有笔记到向量数据库中
     */
    @Test
    void storeEmbedding() {
        List<Document> documentList = new LinkedList<>();

        // 从数据库中查询所有笔记
        List<Notebook> notebookList = notebookService.list();
        notebookList.forEach(notebook -> {
                documentList.add(new Document() {
                    @Override
                    public String text() {
                        return aliOssUtil.getMdFileContent(notebook.getFileUrl());
                    }

                    @Override
                    public Metadata metadata() {
                        Map<String, String> metadataMap = new HashMap<>();
                        metadataMap.put("notebookName", notebook.getFileName());
                        return new Metadata(metadataMap);
                    }
                });
        });

        // 构建文档切割器：最大500个字符，两个片段重复100个字符
        DocumentSplitter documentSplitter = DocumentSplitters.recursive(500, 100);

        // 构建一个EmbeddingStoreIngestor对象，完成文本数据切割，向量化，存储
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(documentSplitter)
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        ingestor.ingest(documentList);
    }

}