package org.example.htuoj.common.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class CommentGetListRespDTO {
    @ApiModelProperty(value = "评论主键ID")
    private Long commentId;

    @ApiModelProperty(value = "文章ID")
    private Long articleId;

    @ApiModelProperty(value = "学生ID")
    private Long userId;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "是否点赞")
    private Boolean isLike;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "父评论ID")
    private Long parentId;

    @ApiModelProperty(value = "子评论总数")
    private Integer childCount;

    @ApiModelProperty(value = "点赞次数")
    private Integer likeCount;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;


}
