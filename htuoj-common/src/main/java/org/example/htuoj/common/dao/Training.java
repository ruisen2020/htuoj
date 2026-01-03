package org.example.htuoj.common.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 训练题单表
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("training")
@ApiModel(value = "Training对象", description = "训练题单表")
public class Training extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "training_id", type = IdType.AUTO)
    private Long trainingId;

    @ApiModelProperty(value = "题单类型（官方题单、个人题单等）")
    @TableField("type")
    private Integer type;

    @ApiModelProperty(value = "	训练题单名称")
    @TableField("title")
    private String title;

    @ApiModelProperty(value = "训练题单简介")
    @TableField("description")
    private String description;

    @ApiModelProperty(value = "   官方推荐题单的排序优先级")
    @TableField("priority")
    private Integer priority;

    @ApiModelProperty(value = "作者用户ID")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty(value = "状态（是否私有）")
    @TableField("state")
    private Integer state;


    @ApiModelProperty(value = "题目总数")
    @TableField("problem_count")
    private Integer problemCount;


    @ApiModelProperty(value = "收藏次数")
    @TableField("collect_count")
    private Integer collectCount;



}
