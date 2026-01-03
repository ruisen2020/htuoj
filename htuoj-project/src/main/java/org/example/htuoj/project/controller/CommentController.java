package org.example.htuoj.project.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.convention.result.Results;
import org.example.htuoj.common.dto.req.CommentAddReqDTO;
import org.example.htuoj.common.dto.req.CommentDeleteReqDTO;
import org.example.htuoj.common.dto.req.CommentGetListReqDTO;
import org.example.htuoj.common.dto.resp.CommentGetListRespDTO;
import org.example.htuoj.project.service.ICommentService;

/**
 * <p>
 * 评论表 前端控制器
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-26
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private ICommentService commentService;

    @PostMapping("/getCommentList")
    public Result<IPage<CommentGetListRespDTO>> getCommentList(@RequestBody CommentGetListReqDTO reqDTO) {
        return Results.success(commentService.getCommentList(reqDTO));
    }

    @PostMapping("/addComment")
    @SaCheckLogin
    public Result<Void> addComment(@RequestBody CommentAddReqDTO reqDTO) {
        commentService.addComment(reqDTO);
        return Results.success();
    }

    @PostMapping("/deleteComment")
    @SaCheckLogin
    public Result<Void> deleteComment(@RequestBody CommentDeleteReqDTO reqDTO) {
        commentService.deleteComment(reqDTO);
        return Results.success();
    }


}
