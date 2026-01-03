package org.example.htuoj.common.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserAddReqDTO {
    @ApiModelProperty(value = "用户姓名")
    private String name;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "学号")
    private String number;

    @ApiModelProperty(value = "邮箱")
    private String mail;

    @ApiModelProperty(value = "验证码")
    private String captcha;

}
