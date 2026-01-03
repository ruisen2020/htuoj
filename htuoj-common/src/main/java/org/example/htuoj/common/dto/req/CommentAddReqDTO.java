package org.example.htuoj.common.dto.req;

import lombok.Data;

@Data
public class CommentAddReqDTO {
    private Long articleId;
    private String content;
    private Long parentId;
}
