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

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("contest_problem")
@ApiModel(value = "contestProblem对象", description = "竞赛题目表")
public class ContestProblem  extends BaseDO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "contest_problem_id", type = IdType.AUTO)
    private Long contestProblemId;

    @ApiModelProperty(value = "竞赛ID")
    @TableField("contest_id")
    private Long contestId;

    @ApiModelProperty(value = "题目ID")
    @TableField("problem_id")
    private Long problemId;

    @ApiModelProperty(value = "题目编号")
    @TableField("display_id")
    private String displayId;

    @ApiModelProperty(value = "通过次数")
    @TableField("accept_count")
    private Integer acceptCount;

    @ApiModelProperty(value = "提交次数")
    @TableField("submit_count")
    private Integer submitCount;
}
