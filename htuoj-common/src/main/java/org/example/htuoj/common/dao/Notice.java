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
 * 点赞表
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("notice")
@ApiModel(value = "Notice对象", description = "通知表")
public class Notice extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "notice_id", type = IdType.AUTO)
    private Long noticeId;

    @ApiModelProperty(value = "发送通知的用户ID")
    private Long senderId;

    @ApiModelProperty(value = "接收通知的用户ID")
    private Long receiverId;

    @ApiModelProperty(value = "类型(0:点赞文章、1：点赞评论、2：评论文章、3：回复评论、4：关注、5：收藏文章、6：收藏题单）")
    private Integer targetType;

    @ApiModelProperty(value = "目标ID（文章ID、评论ID）")
    private Long targetId;

    @ApiModelProperty(value = "状态(0:未读，1：已读)")
    private Boolean isRead;

}
