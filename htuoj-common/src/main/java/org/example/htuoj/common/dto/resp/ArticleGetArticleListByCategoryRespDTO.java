package org.example.htuoj.common.dto.resp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
@Data
public class ArticleGetArticleListByCategoryRespDTO {

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

    @ApiModelProperty(value = "是否点赞")
    private Boolean isLike;

    @ApiModelProperty(value = "收藏总数")
    private Integer collectCount;

    @ApiModelProperty(value = "是否收藏")
    private Boolean isCollect;

    @ApiModelProperty(value = "评论总数")
    private Integer commentCount;

    @ApiModelProperty(value = "观看次数")
    private Integer watchCount;
//
//    @ApiModelProperty(value = "封面图片链接")
//    private String coverUrl;


//    @ApiModelProperty(value = "分类ID（1：讨论 2：分享 3：题解 4：通知）")
//    private Boolean categoryId;

//    @ApiModelProperty(value = "题目Id")
//    private Long problemId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

//    @ApiModelProperty(value = "题目标题")
//    private String problemTitle;
}
