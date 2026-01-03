package org.example.htuoj.common.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LabelAddReqDTO {
    @ApiModelProperty(value = "标签名")
    private String labelName;
}
