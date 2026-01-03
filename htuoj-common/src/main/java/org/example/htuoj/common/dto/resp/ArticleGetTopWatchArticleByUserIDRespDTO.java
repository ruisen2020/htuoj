package org.example.htuoj.common.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ArticleGetTopWatchArticleByUserIDRespDTO {
    @ApiModelProperty(value = "主键ID")
    private Long articleId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "观看次数")
    private Integer watchCount;
}
