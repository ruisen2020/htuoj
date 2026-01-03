package org.example.htuoj.common.dto.req;

import lombok.Data;

@Data
public class ArticleGetByIdReqDTO {
    private Long articleId;

    private Long userId;

    private Integer targetType;
}
