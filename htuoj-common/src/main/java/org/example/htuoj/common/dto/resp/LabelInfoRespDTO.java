package org.example.htuoj.common.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LabelInfoRespDTO {

    @ApiModelProperty(value = "标签名")
    private String labelName;
}
