package org.example.htuoj.project.service;

import org.example.htuoj.common.dao.Article;
import org.example.htuoj.common.dao.ArticleDoc;
import org.example.htuoj.common.dto.req.ArticleGetArticleListReqDTO;
import org.example.htuoj.common.dto.resp.SearchArticleRespDTO;

public interface ISearchService {
    SearchArticleRespDTO searchArticle(ArticleGetArticleListReqDTO reqDTO);

    void addArticleToEs(ArticleDoc articleDoc);

    void deleteArticleFromEs(Long articleId);

    void updateArticleToEs(ArticleDoc articleDoc);
}
