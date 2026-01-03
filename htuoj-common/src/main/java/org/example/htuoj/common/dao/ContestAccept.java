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
@TableName("contest_accept")
@ApiModel(value = "ContestAccept对象", description = "竞赛题目通过表")
public class ContestAccept extends   BaseDO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "contest_accept_id", type = IdType.AUTO)
    private Long contestAcceptId;

    @ApiModelProperty(value = "用户ID")
    @TableField(value = "user_id")
    private Long userId;

    @ApiModelProperty(value = "比赛ID")
    @TableField(value = "contest_id")
    private Long contestId;

    @ApiModelProperty(value = "题目ID")
    @TableField(value = "problem_id")
    private Long problemId;

    @ApiModelProperty(value = "是否通过")
    @TableField(value = "is_accept")
    private Integer isAccept;

    @ApiModelProperty(value = "不通过次数")
    @TableField(value = "wa_sum")
    private Integer waSum;


    @ApiModelProperty(value = "罚时")
    @TableField("penalty_time")
    private Integer penaltyTime;
}
