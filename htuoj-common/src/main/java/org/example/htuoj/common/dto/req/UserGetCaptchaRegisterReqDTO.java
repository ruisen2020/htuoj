package org.example.htuoj.common.dto.req;

import lombok.Data;

@Data
public class UserGetCaptchaRegisterReqDTO {
    private String number;
    private String mail;
    private String userName;
}
