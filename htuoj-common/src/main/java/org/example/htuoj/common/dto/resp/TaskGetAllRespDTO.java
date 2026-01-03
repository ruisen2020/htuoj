package org.example.htuoj.common.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
@Data
public class TaskGetAllRespDTO {
    @ApiModelProperty(value = "任务主键ID")
    private Long taskId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "截止时间")
    private Date deadline;

    @ApiModelProperty(value = "是否完成")
    private Boolean isCompleted;

}
