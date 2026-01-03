package org.example.htuoj.project.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.example.htuoj.common.dao.ArticleDoc;
import org.example.htuoj.common.dto.req.*;
import org.example.htuoj.common.dto.resp.*;

import org.example.htuoj.project.service.ISearchService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.htuoj.common.convention.exception.ClientException;
import org.example.htuoj.common.dao.Article;
import org.example.htuoj.common.mapper.ArticleMapper;
import org.example.htuoj.project.service.IArticleService;
import org.example.htuoj.common.utils.UserHolder;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static org.example.htuoj.common.utils.LockConstant.ARTICLE_LOCK_PREFIX;
import static org.example.htuoj.project.enums.ArticleErrorCodeEnum.ARTICLE_ADD_ERROR;
import static org.example.htuoj.project.enums.ArticleErrorCodeEnum.ARTICLE_DELETE_ERROR;

/**
 * <p>
 * 文章(讨论、分享、题解等） 服务实现类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-23
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ISearchService searchService;

    @Override
    public void addArticle(ArticleAddReqDTO reqDTO) {
        Article article = new Article();
        BeanUtils.copyProperties(reqDTO, article);
        article.setUserId(UserHolder.getUserId());
        if (reqDTO.getCoverUrl() == null) {
            if (reqDTO.getCategoryId() == 3) {
                article.setCoverUrl("https://xiaoxin18.oss-cn-hangzhou.aliyuncs.com/htuoj/article/2:06b05ed0-ef13-4d28-91f4-bbd0e993e845.png");
            } else if (reqDTO.getCategoryId() == 4) {
                article.setCoverUrl("https://xiaoxin18.oss-cn-hangzhou.aliyuncs.com/htuoj/article/2:231f8f20-c4c6-4acc-a29e-4d9a9c840203.png");
            } else if (reqDTO.getCategoryId() == 1) {
                article.setCoverUrl("https://xiaoxin18.oss-cn-hangzhou.aliyuncs.com/htuoj/article/2:3b88ce2d-bd56-4438-817b-3b328989d0f2.png");
            } else if (reqDTO.getCategoryId() == 2) {
                article.setCoverUrl("https://xiaoxin18.oss-cn-hangzhou.aliyuncs.com/htuoj/article/2:52baa60b-aa4c-4f24-8373-a76876530284.png");
            }
        }
        try {
            articleMapper.insert(article);
            ArticleDoc articleDoc = articleMapper.addArticleDoc(article.getArticleId());
            searchService.addArticleToEs(articleDoc);
        } catch (Exception e) {
            throw new ClientException(ARTICLE_ADD_ERROR);
        }

    }

    @Override
    public IPage<ArticleGetArticleListRespDTO> getArticleList(ArticleGetArticleListReqDTO reqDTO) {
        reqDTO.setUserId(UserHolder.getUserId());
        IPage<ArticleGetArticleListRespDTO> result = null;
        if (reqDTO.getCategoryId() == null || reqDTO.getCategoryId() != 5) {
            result = articleMapper.getArticleList(reqDTO);
        } else {
            result = articleMapper.getFollowArticleList(reqDTO);
        }
        return result;
    }

    @Override
    public ArticleGetArticleByIdRespDTO getArticleById(Long articleId) {
        ArticleGetByIdReqDTO reqDTO = new ArticleGetByIdReqDTO();
        reqDTO.setArticleId(articleId);
        reqDTO.setUserId(UserHolder.getUserId());
        reqDTO.setTargetType(0);
        ArticleGetArticleByIdRespDTO result = articleMapper.getArticleById(reqDTO);
        // 增加浏览量
        this.update().setSql("watch_count = watch_count + 1")
                .eq("article_id", reqDTO.getArticleId())
                .update();
//        RLock lock = redissonClient.getLock(ARTICLE_LOCK_PREFIX + articleId);
//        try {
//            boolean ok = lock.tryLock(1, 10, TimeUnit.SECONDS);
//            if (ok) {
//                this.update().setSql("watch_count = watch_count + 1")
//                        .eq("article_id", reqDTO.getArticleId())
//                        .update();
//            } else {
//                throw new ClientException("服务器错误");
//            }
//        } catch (Exception e) {
//            throw new ClientException("服务器错误");
//        } finally {
//            lock.unlock();
//        }
        return result;
    }

    @Override
    public IPage<ArticleGetTopWatchArticleByUserIDRespDTO> getTopWatchArticleByUserID(ArticleGetTopWatchArticleByUserIDReqDTO reqDTO) {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getUserId, reqDTO.getUserId());
        articleLambdaQueryWrapper.orderByDesc(Article::getWatchCount);
        ArticleGetTopWatchArticleByUserIDReqDTO result = articleMapper.selectPage(reqDTO, articleLambdaQueryWrapper);
        IPage<ArticleGetTopWatchArticleByUserIDRespDTO> convert = result.convert(new Function<Article, ArticleGetTopWatchArticleByUserIDRespDTO>() {
            @Override
            public ArticleGetTopWatchArticleByUserIDRespDTO apply(Article article) {
                ArticleGetTopWatchArticleByUserIDRespDTO articleByUserIDRespDTO = new ArticleGetTopWatchArticleByUserIDRespDTO();
                articleByUserIDRespDTO.setArticleId(article.getArticleId());
                articleByUserIDRespDTO.setTitle(article.getTitle());
                articleByUserIDRespDTO.setWatchCount(article.getWatchCount());
                return articleByUserIDRespDTO;
            }

        });
        return convert;
    }

    @Override
    public void deleteArticle(Long articleId) {
        // 这里需要加校验
        // 判断是不是自己的文章
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            throw new ClientException("文章不存在");
        }
        if (!Objects.equals(article.getUserId(), UserHolder.getUserId())) {
            throw new ClientException("不能删除别人的文章");
        }
        try {
            this.removeById(articleId);

            searchService.deleteArticleFromEs(articleId);
        } catch (Exception e) {
            throw new ClientException(ARTICLE_DELETE_ERROR);
        }

    }

    @Override
    public IPage<ArticleGetArticleListByUserIdRespDTO> getArticleListByUserId(ArticleGetArticleListByUserIdReqDTO reqDTO) {
        reqDTO.setMyUserId(UserHolder.getUserId());
        IPage<ArticleGetArticleListByUserIdRespDTO> result = null;
        result = articleMapper.getArticleListByUserId(reqDTO);
        return result;
    }

    @Override
    public IPage<ArticleGetArticleListByCategoryRespDTO> getArticleListByCategory(ArticleGetArticleListByCategoryReqDTO reqDTO) {
        reqDTO.setUserId(UserHolder.getUserId());
        IPage<ArticleGetArticleListByCategoryRespDTO> result = articleMapper.getArticleListByCategory(reqDTO);
        return result;
    }

    @Override
    public void updateArticle(ArticleUpdateArticleReqDTO reqDTO) {
        Article byId = this.getById(reqDTO.getArticleId());
        if (byId == null) {
            throw new ClientException("文章不存在");
        }
        if (!byId.getUserId().equals(UserHolder.getUserId())) {
            throw new ClientException("没有权限修改该文章");
        }
        Article article = new Article();
        BeanUtils.copyProperties(reqDTO, article);
        this.updateById(article);

        ArticleDoc articleDoc = articleMapper.addArticleDoc(article.getArticleId());
        searchService.updateArticleToEs(articleDoc);
    }
}
