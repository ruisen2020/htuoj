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
 * 评论表
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("comment")
@ApiModel(value="Comment对象", description="评论表")
public class Comment extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "评论主键ID")
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Long commentId;

    @ApiModelProperty(value = "文章ID")
    private Long articleId;

    @ApiModelProperty(value = "学生ID")
    private Long userId;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "父评论ID")
    private Long parentId;

    @ApiModelProperty(value = "子评论总数")
    private Integer childCount;

    @ApiModelProperty(value = "点赞次数")
    private Integer likeCount;

    @ApiModelProperty(value = "是否被删除")
    private Integer state;




}
