package org.example.htuoj.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.htuoj.common.dao.Article;
import org.example.htuoj.common.dto.req.*;
import org.example.htuoj.common.dto.resp.*;

/**
 * <p>
 * 文章(讨论、分享、题解等） 服务类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-23
 */
public interface IArticleService extends IService<Article> {

    void addArticle(ArticleAddReqDTO reqDTO);

    IPage<ArticleGetArticleListRespDTO> getArticleList(ArticleGetArticleListReqDTO reqDTO);

    ArticleGetArticleByIdRespDTO getArticleById(Long articleId);

    IPage<ArticleGetTopWatchArticleByUserIDRespDTO> getTopWatchArticleByUserID(ArticleGetTopWatchArticleByUserIDReqDTO reqDTO);

    void deleteArticle(Long articleId);

    IPage<ArticleGetArticleListByUserIdRespDTO> getArticleListByUserId(ArticleGetArticleListByUserIdReqDTO reqDTO);

    IPage<ArticleGetArticleListByCategoryRespDTO> getArticleListByCategory(ArticleGetArticleListByCategoryReqDTO reqDTO);

    void updateArticle(ArticleUpdateArticleReqDTO reqDTO);
}
