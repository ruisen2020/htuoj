package org.example.htuoj.common.dto.resp;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

@Data
public class ContestGetListRespDTO {
    private Long contestId;
    private String title;
    private Date startTime;
    private Date endTime;
    private String duration;
    private String description;
    @ApiModelProperty(value = "比赛状态（0：未开始，1：进行中，2：已结束")
    private Integer status;
    private Integer registerSum;
    private String makers;
    private String  usernames;
    private Boolean  isRegister;
}
