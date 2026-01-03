package org.example.htuoj.common.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ArticleUpdateArticleReqDTO {
    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "封面图片链接")
    private String coverUrl;

    private Long articleId;

}
