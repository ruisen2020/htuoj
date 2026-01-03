package org.example.htuoj.common.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserLoginReqDTO {

    @ApiModelProperty(value = "学号/工号")
    private String number;

    @ApiModelProperty(value = "密码")
    private String password;
}
