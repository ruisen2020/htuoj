package org.example.htuoj.common.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 文章(讨论、分享、题解等）
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("article")
@ApiModel(value = "Article对象", description = "文章(讨论、分享、题解等）")
public class Article extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "article_id", type = IdType.AUTO)
    private Long articleId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "作者用户ID")
    private Long userId;

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

    @ApiModelProperty(value = "文章状态（公开、隐藏、是否通过审核等）")
    private Integer state;

    @ApiModelProperty(value = "关联题目ID")
    private Long problemId;

}
