package com.lbytech.lbytech_backend_new.es;

import cn.hutool.json.JSONUtil;
import com.lbytech.lbytech_backend_new.exception.BusinessException;
import com.lbytech.lbytech_backend_new.pojo.Enum.StatusCodeEnum;
import com.lbytech.lbytech_backend_new.properties.ElasticsearchProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


/**
 * Elasticsearch服务实现类
 */
@Service
@Slf4j
public class NotebookEsServiceImpl implements INotebookEsService {

    @Autowired
    private ElasticsearchProperties elasticsearchProperties;

    private RestHighLevelClient restHighLevelClient = null;

    @Override
    public void createIndex() {
        restHighLevelClient = new RestHighLevelClient(RestClient.builder(
                HttpHost.create(elasticsearchProperties.getHost())
        ));

        // Request对象
        CreateIndexRequest request = new CreateIndexRequest("notebook"); // "notebook" 索引名称
        // 请求参数
        request.source(EsConstant.CREATE_INDEX_TEMPLATE, XContentType.JSON);
        // 发送请求
        try {
            restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new BusinessException(StatusCodeEnum.FAIL, "创建ES索引失败");
        } finally {
            try {
                restHighLevelClient.close();
            } catch (IOException e) {
                log.error("关闭ES客户端失败", e);
            }
        }
    }

    @Override
    public void saveOrUpdate(NotebookForEs notebookForEs) {
        restHighLevelClient = new RestHighLevelClient(RestClient.builder(
                HttpHost.create(elasticsearchProperties.getHost())
        ));

        IndexRequest request = new IndexRequest("notebook")
                .id(notebookForEs.getId().toString())
                .source(JSONUtil.toJsonStr(notebookForEs), XContentType.JSON);
        try {
            restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new BusinessException(StatusCodeEnum.FAIL, "保存笔记到es失败");
        } finally {
            try {
                restHighLevelClient.close();
            } catch (IOException e) {
                log.error("关闭es客户端失败");
            }
        }
    }
}