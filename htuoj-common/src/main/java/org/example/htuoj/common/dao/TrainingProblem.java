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
 * 训练题单与题目关联表
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("training_problem")
@ApiModel(value = "TrainingProblem对象", description = "训练题单与题目关联表")
public class TrainingProblem extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "training_problem_id", type = IdType.AUTO)
    private Long trainingProblemId;

    @ApiModelProperty(value = "训练题单ID")
    private Long trainingId;

    @ApiModelProperty(value = "题目ID")
    private Long problemId;

    @ApiModelProperty(value = "排序")
    @TableField("`order`")
    private Integer order;
}
