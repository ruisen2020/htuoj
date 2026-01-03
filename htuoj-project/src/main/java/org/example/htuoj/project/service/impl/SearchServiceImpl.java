package org.example.htuoj.project.service.impl;

import cn.hutool.json.JSONUtil;
import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.xcontent.XContentType;
import org.example.htuoj.common.convention.exception.ClientException;
import org.example.htuoj.common.dao.Article;
import org.example.htuoj.common.dao.ArticleDoc;
import org.example.htuoj.common.dto.req.ArticleGetArticleListReqDTO;
import org.example.htuoj.common.dto.resp.SearchArticleRespDTO;
import org.example.htuoj.project.service.ISearchService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static org.example.htuoj.project.enums.ArticleErrorCodeEnum.ARTICLE_ADD_ERROR;
import static org.example.htuoj.project.enums.ArticleErrorCodeEnum.ARTICLE_DELETE_ERROR;

@Service
public class SearchServiceImpl implements ISearchService {

    private final RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
            HttpHost.create("http://117.72.110.81:9200")
    ));

    @Override
    public SearchArticleRespDTO searchArticle(ArticleGetArticleListReqDTO reqDTO) {

        SearchRequest searchRequest = new SearchRequest("article");
        // 创建 SearchSourceBuilder 对象
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 设置 from 和 size
        sourceBuilder.from((int) ((reqDTO.getCurrent() - 1) * reqDTO.getSize()));
        sourceBuilder.size((int) reqDTO.getSize());

        // 设置 track_total_hits 为 true 以获取精确的总数
        sourceBuilder.trackTotalHits(true);
        // 创建 MultiMatchQueryBuilder 对象
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(reqDTO.getSearchContent(), "title", "userName", "content");
        // 创建 BoolQueryBuilder 对象并将 multiMatchQueryBuilder 和 termQueryBuilder 添加到 must 子句中
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(multiMatchQueryBuilder);
        if (reqDTO.getCategoryId() != null) {
            // 创建 TermQueryBuilder 对象来过滤 categoryId 为 1
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("categoryId", reqDTO.getCategoryId());
            boolQueryBuilder.filter(termQueryBuilder);
        }
        // 设置查询
        sourceBuilder.query(boolQueryBuilder);

        // 创建 HighlightBuilder 对象
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field("title");
        highlightTitle.preTags("<em>").postTags("</em>");
        HighlightBuilder.Field highlightUserName = new HighlightBuilder.Field("userName");
        highlightUserName.preTags("<em>").postTags("</em>");
        HighlightBuilder.Field highlightContent = new HighlightBuilder.Field("content");
        highlightContent.preTags("<em>").postTags("</em>");

        // 添加高亮字段
        highlightBuilder.field(highlightTitle);
        highlightBuilder.field(highlightUserName);
        highlightBuilder.field(highlightContent);

        // 设置高亮
        sourceBuilder.highlighter(highlightBuilder);

        // 将 SearchSourceBuilder 设置到 SearchRequest
        searchRequest.source(sourceBuilder);

        // 执行搜索请求
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 处理响应
        SearchHits searchHits = searchResponse.getHits();
        // 1.获取总条数
        long total = searchHits.getTotalHits().value;
        // 2.遍历结果数组
        SearchHit[] hits = searchHits.getHits();
        ArrayList<ArticleDoc> articleDocs = new ArrayList<>();
        for (SearchHit hit : hits) {
            // 3.得到_source，也就是原始json文档
            String source = hit.getSourceAsString();

            // 4.反序列化并打印
            ArticleDoc item = JSONUtil.toBean(source, ArticleDoc.class);
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            for (String key : highlightFields.keySet()) {
                if (key.equals("userName")) {
                    HighlightField userName = highlightFields.get("userName");
                    item.setUserName(userName.getFragments()[0].string());
                } else if (key.equals("title")) {
                    HighlightField title = highlightFields.get("title");
                    item.setTitle(title.getFragments()[0].string());
                } else if (key.equals("content")) {
                    HighlightField content = highlightFields.get("content");
                    item.setContent(content.getFragments()[0].string());
                }
            }
            articleDocs.add(item);
        }
        SearchArticleRespDTO result = new SearchArticleRespDTO();
        result.setTotal(hits.length);
        result.setRecords(articleDocs);
        return result;
    }

    @Override
    public void addArticleToEs(ArticleDoc articleDoc) {
        String doc = JSONUtil.toJsonStr(articleDoc);
        // 1.准备Request对象
        IndexRequest request = new IndexRequest("article").id(String.valueOf(articleDoc.getArticleId()));
        // 2.准备Json文档
        request.source(doc, XContentType.JSON);
        // 3.发送请求
        try {
            client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new ClientException(ARTICLE_ADD_ERROR);
        }
    }

    @Override
    public void deleteArticleFromEs(Long articleId) {
        DeleteRequest request = new DeleteRequest("article", String.valueOf(articleId));
        try {
            client.delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new ClientException(ARTICLE_DELETE_ERROR);
        }
    }

    @Override
    public void updateArticleToEs(ArticleDoc articleDoc) {
        String doc = JSONUtil.toJsonStr(articleDoc);
        // 1.准备Request对象
        UpdateRequest request = new UpdateRequest("article", String.valueOf(articleDoc.getArticleId()));
        // 2.准备Json文档
        request.doc(doc, XContentType.JSON);
        // 3.发送请求
        try {
            client.update(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new ClientException(ARTICLE_ADD_ERROR);
        }
    }
}
