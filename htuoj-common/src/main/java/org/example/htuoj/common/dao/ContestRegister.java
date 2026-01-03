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
import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("contest_register")
@ApiModel(value = "ContestRegister对象", description = "竞赛报名表")
public class ContestRegister extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "contest_register_id", type = IdType.AUTO)
    private Long contestRegisterId;

    @ApiModelProperty(value = "竞赛ID")
    @TableField("contest_id")
    private Long contestId;

    @ApiModelProperty(value = "用户ID")
    @TableField("user_id")
    private Long userId;


    @ApiModelProperty(value = "罚时")
    @TableField("penalty_time")
    private Long penaltyTime;


    @ApiModelProperty(value = "通过题目总数")
    @TableField("accept_sum")
    private Integer acceptSum;
}
