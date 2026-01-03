package org.example.htuoj.common.dto.resp;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.example.htuoj.common.dao.AwardInfo;

@Data
public class AwardInfoGetAllRespDTO extends Page<AwardInfo> {

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "学生姓名")
    private String studentName;

    @ApiModelProperty(value = "学号")
    private String studentNumber;

    @ApiModelProperty(value = "学生所属学院")
    private String studentCollege;

    @ApiModelProperty(value = "竞赛名称")
    private String competitionName;

    @ApiModelProperty(value = "赛道名称")
    private String trackName;

    @ApiModelProperty(value = "获奖级别")
    private String awardLevel;

    @ApiModelProperty(value = "获奖时间")
    private String awardTime;
}
