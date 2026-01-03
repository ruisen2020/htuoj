package org.example.htuoj.common.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AwardInfoGetByUserIDRespDTO {


    @ApiModelProperty(value = "竞赛名称")
    private String competitionName;

    @ApiModelProperty(value = "赛道名称")
    private String trackName;

    @ApiModelProperty(value = "获奖级别")
    private String awardLevel;

    @ApiModelProperty(value = "获奖时间")
    private String awardTime;
}
