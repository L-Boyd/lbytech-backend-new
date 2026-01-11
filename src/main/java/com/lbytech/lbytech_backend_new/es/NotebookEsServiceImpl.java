package com.lbytech.lbytech_backend_new.es;

import cn.hutool.core.collection.CollUtil;
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
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Elasticsearch服务实现类
 */
@Service
@Slf4j
public class NotebookEsServiceImpl implements INotebookEsService {

    @Autowired
    private ElasticsearchProperties elasticsearchProperties;

    private RestHighLevelClient restHighLevelClient = null;

    private void beforeRequest() {
        restHighLevelClient = new RestHighLevelClient(RestClient.builder(
                HttpHost.create(elasticsearchProperties.getHost())
        ));
    }

    private void afterRequest() {
        try {
            restHighLevelClient.close();
        } catch (IOException e) {
            log.error("关闭es客户端失败");
        }
    }

    @Override
    public void createIndex() {
        beforeRequest();

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
            afterRequest();
        }
    }

    @Override
    public void saveOrUpdate(NotebookForEs notebookForEs) {
        beforeRequest();

        IndexRequest request = new IndexRequest("notebook")
                .id(notebookForEs.getId().toString())
                .source(JSONUtil.toJsonStr(notebookForEs), XContentType.JSON);
        try {
            restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new BusinessException(StatusCodeEnum.FAIL, "保存笔记到es失败");
        } finally {
            afterRequest();
        }
    }

    @Override
    public List<NotebookForEsVO> getByKeywordContaining(String keyword, int page, int size) {
        beforeRequest();

        SearchRequest searchRequest = new SearchRequest("notebook");
        searchRequest.source()
                .query(QueryBuilders.boolQuery()
                        .should(QueryBuilders.matchQuery("content", keyword))
                        .should(QueryBuilders.matchQuery("fileName", keyword)))
                .from((page - 1) * size)
                .size(size)
                .highlighter(SearchSourceBuilder.highlight()
                        .field("content")
                        .field("fileName")
                        .fragmentSize(50)
                        .numOfFragments(2));

        try {
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits searchHits = response.getHits();
            SearchHit[] hits = searchHits.getHits();

            List<NotebookForEsVO> notebookForEsVOList = new LinkedList<>();
            for (SearchHit hit : hits) {
                NotebookForEsVO notebookForEsVO = JSONUtil.toBean(hit.getSourceAsString(), NotebookForEsVO.class);

                // 处理高亮结果
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                List<String> highlightStringList = new ArrayList<>();
                if (CollUtil.isNotEmpty(highlightFields)) {
                    HighlightField highlightField1 = highlightFields.get("fileName");
                    HighlightField highlightField2 = highlightFields.get("content");
                    if (highlightField1 != null) {
                        Text[] fragments = highlightField1.getFragments();
                        for (Text text : fragments) {
                            highlightStringList.add(text.string());
                        }
                    }
                    if (highlightField2 != null) {
                        Text[] fragments = highlightField2.getFragments();
                        for (Text text : fragments) {
                            highlightStringList.add(text.string());
                        }
                    }
                }
                notebookForEsVO.setHighlightContent(highlightStringList);

                notebookForEsVOList.add(notebookForEsVO);
            }
            return notebookForEsVOList;
        } catch (IOException e) {
            throw new BusinessException(StatusCodeEnum.FAIL, "ES搜索异常");
        } finally {
            afterRequest();
        }
    }
}