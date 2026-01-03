package org.example.htuoj.common.dao;

import com.alibaba.excel.annotation.ExcelProperty;
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
 * 获奖信息
 * </p>
 *
 * @author xiaoxin
 * @since 2024-09-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("award_info")
@ApiModel(value = "AwardInfo对象", description = "获奖信息")
public class AwardInfo extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "学生姓名")
    @ExcelProperty("姓名")
    private String studentName;

    @ApiModelProperty(value = "学号")
    private String studentNumber;

    @ApiModelProperty(value = "学生所属学院")
    private String studentCollege;

    @ApiModelProperty(value = "竞赛名称")
    private String competitionName;

    @ApiModelProperty(value = "赛道名称")
    @ExcelProperty("科目名称")
    private String trackName;

    @ApiModelProperty(value = "获奖级别")
    @ExcelProperty("奖项")
    private String awardLevel;

    @ApiModelProperty(value = "获奖时间")
    private String awardTime;

}
