package org.example.htuoj.common.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubmissionGetListByProblemIdRespDTO {

    @ApiModelProperty(value = "主键ID")
    private Long submissionId;


    @ApiModelProperty(value = "status")
    private Integer status;

    @ApiModelProperty(value = "运行时间")
    private Integer time;

    @ApiModelProperty(value = "使用内存")
    private Integer memory;

    @ApiModelProperty(value = "创建时间/提交时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "提交语言")
    private Integer language;
}
