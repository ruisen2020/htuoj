package org.example.htuoj.common.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import feign.Param;
import org.example.htuoj.common.dao.Article;
import org.example.htuoj.common.dao.ArticleDoc;
import org.example.htuoj.common.dto.req.ArticleGetArticleListByCategoryReqDTO;
import org.example.htuoj.common.dto.req.ArticleGetArticleListByUserIdReqDTO;
import org.example.htuoj.common.dto.req.ArticleGetArticleListReqDTO;
import org.example.htuoj.common.dto.req.ArticleGetByIdReqDTO;
import org.example.htuoj.common.dto.resp.ArticleGetArticleByIdRespDTO;
import org.example.htuoj.common.dto.resp.ArticleGetArticleListByCategoryRespDTO;
import org.example.htuoj.common.dto.resp.ArticleGetArticleListByUserIdRespDTO;
import org.example.htuoj.common.dto.resp.ArticleGetArticleListRespDTO;

/**
 * <p>
 * 文章(讨论、分享、题解等） Mapper 接口
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-23
 */
public interface ArticleMapper extends BaseMapper<Article> {


    IPage<ArticleGetArticleListRespDTO> getArticleList(ArticleGetArticleListReqDTO reqDTO);


    ArticleGetArticleByIdRespDTO getArticleById(ArticleGetByIdReqDTO reqDTO);

    IPage<ArticleGetArticleListRespDTO> getFollowArticleList(ArticleGetArticleListReqDTO reqDTO);

    IPage<ArticleGetArticleListByUserIdRespDTO> getArticleListByUserId(ArticleGetArticleListByUserIdReqDTO reqDTO);

    IPage<ArticleGetArticleListByCategoryRespDTO> getArticleListByCategory(ArticleGetArticleListByCategoryReqDTO reqDTO);

    ArticleDoc addArticleDoc(@Param("articleId") long l);
}
