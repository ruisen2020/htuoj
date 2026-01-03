package org.example.htuoj.common.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
@Data
public class ArticleDoc {
    @ApiModelProperty(value = "主键ID")
    private Long articleId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "作者用户ID")
    private Long userId;

    @ApiModelProperty(value = "作者用户名")
    private String userName;

    @ApiModelProperty(value = "作者用户头像")
    private String avatar;

    @ApiModelProperty(value = "点赞总数")
    private Integer likeCount;


    @ApiModelProperty(value = "收藏总数")
    private Integer collectCount;


    @ApiModelProperty(value = "评论总数")
    private Integer commentCount;

    @ApiModelProperty(value = "观看次数")
    private Integer watchCount;

    @ApiModelProperty(value = "封面图片链接")
    private String coverUrl;

    @ApiModelProperty(value = "分类ID（1：讨论 2：分享 3：题解 4：通知）")
    private Integer categoryId;

}
