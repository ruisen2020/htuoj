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
@TableName("`like`")
@ApiModel(value = "Like对象", description = "点赞表")
public class Like extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "like_id", type = IdType.AUTO)
    private Long likeId;

    @ApiModelProperty(value = "点赞的学生ID")
    private Long userId;

    @ApiModelProperty(value = "目标类型")
    private Integer targetType;

    @ApiModelProperty(value = "目标ID")
    private Long targetId;

    @ApiModelProperty(value = "状态(0:已点赞，1：取消点赞）")
    private Integer state;

}
