package org.example.htuoj.common.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 竞赛表
 * </p>
 *
 * @author xiaoxin
 * @since 2024-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("contest")
@ApiModel(value = "Contest对象", description = "竞赛表")
public class Contest extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "竞赛主键ID")
    @TableId(value = "contest_id", type = IdType.AUTO)
    private Long contestId;

    @ApiModelProperty(value = "比赛标题")
    @TableField("title")
    private String title;

    @ApiModelProperty(value = "赛制")
    @TableField("type")
    private Integer type;

    @ApiModelProperty(value = "开始时间")
    @TableField("start_time")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @TableField("end_time")
    private Date endTime;

    @ApiModelProperty(value = "比赛时长(s)")
    @TableField("duration")
    private String duration;

    @ApiModelProperty(value = "比赛说明")
    @TableField("description")
    private String description;

    @ApiModelProperty(value = "比赛状态（0：未开始，1：进行中，2：已结束")
    @TableField("status")
    private Integer status;



}
