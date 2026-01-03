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
 * 关注表
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("follow")
@ApiModel(value = "Follow对象", description = "关注表")
public class Follow extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "关注者")
    private Long followForm;

    @ApiModelProperty(value = "被关注者")
    private Long followTo;

    @ApiModelProperty(value = "关注状态（0：未关注 1：已关注）")
    private Integer state;


}
