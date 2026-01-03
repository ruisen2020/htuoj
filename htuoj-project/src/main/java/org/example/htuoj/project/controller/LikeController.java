package org.example.htuoj.project.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.convention.result.Results;
import org.example.htuoj.common.dto.req.LikeAddReqDTO;
import org.example.htuoj.project.service.ILikeService;

/**
 * <p>
 * 点赞表 前端控制器
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-24
 */
@RestController
@RequestMapping("/like")
public class LikeController {
    @Autowired
    private ILikeService likeService;

    @PostMapping("/addLike")
    @SaCheckLogin
    public Result<Void> addLike(@RequestBody LikeAddReqDTO reqDTO) throws InterruptedException {
        likeService.addLike(reqDTO);
        return Results.success();
    }

//    @PostMapping("/deleteLike")
//    public Result<Void> deleteLike(@RequestBody LikeAddReqDTO reqDTO) throws InterruptedException {
//        likeService.deleteLike(reqDTO);
//        return Results.success();
//    }


}
