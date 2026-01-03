package org.example.htuoj.common.dto.resp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ContestGetContestListByOtherOJRespDTO {

    @ApiModelProperty(value = "url")
    private String url;

    @ApiModelProperty(value = "OJ类型")
    private String OJ;

    @ApiModelProperty(value = "比赛标题")
    private String title;


    @ApiModelProperty(value = "开始时间")
    @TableField("start_time")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @TableField("den_time")
    private Date endTime;

    @ApiModelProperty(value = "比赛时长(s)")
    @TableField("duration")
    private Integer duration;

}
