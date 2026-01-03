package org.example.htuoj.common.dto.resp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class NoticeGetNoticeListRespDTO {

    @ApiModelProperty(value = "主键ID")
    private Long noticeId;

    @ApiModelProperty(value = "发送通知的用户ID")
    private Long senderId;

    @ApiModelProperty(value = "发送通知的用户昵称")
    private String userName;

    @ApiModelProperty(value = "发送通知的用户头像")
    private String avatar;

    @ApiModelProperty(value = "发送通知的时间")
    private Date createTime;

    @ApiModelProperty(value = "类型(0:点赞文章、1：点赞评论、2：评论文章、3：回复评论、4：关注）")
    private Integer targetType;

    @ApiModelProperty(value = "目标ID（文章ID、评论ID）")
    private Long targetId;

    @ApiModelProperty(value = "状态(0:未读，1：已读)")
    private Boolean isRead;

    private Boolean isFollowedByMe;

    private Long articleId;

    private String articleTitle;

    private String articleCover;

    private String commentContent;

    private Long trainingId;
    private String trainingTitle;

}
