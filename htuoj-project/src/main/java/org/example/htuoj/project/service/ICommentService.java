package org.example.htuoj.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.htuoj.common.dao.Comment;
import org.example.htuoj.common.dto.req.CommentAddReqDTO;
import org.example.htuoj.common.dto.req.CommentDeleteReqDTO;
import org.example.htuoj.common.dto.req.CommentGetListReqDTO;
import org.example.htuoj.common.dto.resp.CommentGetListRespDTO;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-26
 */
public interface ICommentService extends IService<Comment> {

    IPage<CommentGetListRespDTO> getCommentList(CommentGetListReqDTO reqDTO);

    void addComment(CommentAddReqDTO reqDTO);

    void deleteComment(CommentDeleteReqDTO reqDTO);
}
