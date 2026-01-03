package org.example.htuoj.common.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.example.htuoj.common.dao.Comment;

@Data
public class CommentGetListReqDTO extends Page<Comment> {
    private Long commentId;
    private Long targetType;
    private Long articleId;
    private Long userId;
}
