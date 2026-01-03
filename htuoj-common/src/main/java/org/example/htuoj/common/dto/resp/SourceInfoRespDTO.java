package org.example.htuoj.common.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SourceInfoRespDTO {
    @ApiModelProperty(value = "来源名称")
    private String sourceName;

}
