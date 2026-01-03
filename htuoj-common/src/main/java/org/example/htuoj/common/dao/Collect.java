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
@TableName("collect")
@ApiModel(value = "Collect对象", description = "点赞表")
public class Collect extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "collect_id", type = IdType.AUTO)
    private Long collectId;

    @ApiModelProperty(value = "点赞的学生ID")
    private Long userId;

    @ApiModelProperty(value = "目标ID")
    private Long targetId;

    @ApiModelProperty(value = "目标类型（0：收藏文章、1：收藏题单）")
    private Integer targetType;

    @ApiModelProperty(value = "状态(0:已收藏，1：取消收藏）")
    private Integer state;


}
