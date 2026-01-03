package org.example.htuoj.project.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.example.htuoj.common.dto.req.*;
import org.example.htuoj.common.dto.resp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.convention.result.Results;
import org.example.htuoj.project.service.IArticleService;

/**
 * <p>
 * 文章(讨论、分享、题解等） 前端控制器
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-23
 */
@RestController
@Slf4j
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private IArticleService articleService;

    @PostMapping("/addArticle")
    @SaCheckLogin
    public Result<Void> addArticle(@RequestBody ArticleAddReqDTO reqDTO) {
        articleService.addArticle(reqDTO);
        return Results.success();
    }


    @PostMapping("/getArticleList")
    public Result<IPage<ArticleGetArticleListRespDTO>> getArticleList(@RequestBody ArticleGetArticleListReqDTO reqDTO) {
        return Results.success(articleService.getArticleList(reqDTO));
    }

    @PostMapping("/getArticleListByUserId")
    public Result<IPage<ArticleGetArticleListByUserIdRespDTO>> getArticleListByUserId(@RequestBody ArticleGetArticleListByUserIdReqDTO reqDTO) {
        return Results.success(articleService.getArticleListByUserId(reqDTO));
    }

    @GetMapping("/getArticleById")
    public Result<ArticleGetArticleByIdRespDTO> getArticleById(@RequestParam("articleId") Long articleId) {
        return Results.success(articleService.getArticleById(articleId));
    }

    @PostMapping("getTopWatchArticleByUserID")
    public Result<IPage<ArticleGetTopWatchArticleByUserIDRespDTO>> getTopWatchArticleByUserID(@RequestBody ArticleGetTopWatchArticleByUserIDReqDTO reqDTO) {
        return Results.success(articleService.getTopWatchArticleByUserID(reqDTO));
    }

    @PostMapping("deleteArticle")
    @SaCheckLogin
    public Result<Void> deleteArticle(@RequestBody ArticleDeleteArticleReqDTO reqDTO) {
        articleService.deleteArticle(reqDTO.getArticleId());
        return Results.success();
    }

    @PostMapping("/getArticleListByCategory")
    public Result<IPage<ArticleGetArticleListByCategoryRespDTO>> getArticleListByCategory(@RequestBody ArticleGetArticleListByCategoryReqDTO reqDTO) {
        return Results.success(articleService.getArticleListByCategory(reqDTO));
    }

    @PostMapping("/updateArticle")
    @SaCheckLogin
    public Result<Void> updateArticle(@RequestBody ArticleUpdateArticleReqDTO reqDTO) {
        articleService.updateArticle(reqDTO);
        return Results.success();
    }
}
