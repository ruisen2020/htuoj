package org.example.htuoj.common.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SourceGetAllRespDTO {

    @ApiModelProperty(value = "主键ID")
    private String value;

    @ApiModelProperty(value = "来源名称")
    private String label;
}
