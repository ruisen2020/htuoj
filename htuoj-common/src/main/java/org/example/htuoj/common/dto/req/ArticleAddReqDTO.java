package org.example.htuoj.common.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ArticleAddReqDTO {

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "封面图片链接")
    private String coverUrl;

    @ApiModelProperty(value = "分类ID（1：讨论 2：分享 3：题解 4：通知）")
    private Integer categoryId;

    @ApiModelProperty(value = "题目ID")
    private Long problemId;
}
