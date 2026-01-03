package org.example.htuoj.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.htuoj.common.dao.Comment;
import org.example.htuoj.common.dto.req.CommentGetListReqDTO;
import org.example.htuoj.common.dto.resp.CommentGetListRespDTO;

/**
 * <p>
 * 评论表 Mapper 接口
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-26
 */
public interface CommentMapper extends BaseMapper<Comment> {

    IPage<CommentGetListRespDTO> getCommentList(CommentGetListReqDTO reqDTO);
}
