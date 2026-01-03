package org.example.htuoj.project.controller;

import cn.hutool.json.JSONUtil;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
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
import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.convention.result.Results;
import org.example.htuoj.common.dao.ArticleDoc;
import org.example.htuoj.common.dto.req.ArticleGetArticleListReqDTO;
import org.example.htuoj.common.dto.resp.SearchArticleRespDTO;
import org.example.htuoj.project.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/search")
public class SearchController {


    @Autowired
    private ISearchService searchService;

    @PostMapping("/searchArticle")
    public Result<SearchArticleRespDTO> searchArticle(@RequestBody ArticleGetArticleListReqDTO reqDTO) throws IOException {
        return Results.success(searchService.searchArticle(reqDTO));

    }
}
