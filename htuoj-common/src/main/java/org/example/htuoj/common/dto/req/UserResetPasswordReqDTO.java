package org.example.htuoj.common.dto.req;

import lombok.Data;

@Data
public class UserResetPasswordReqDTO {
    private String number;

    private String newPassword;

    private String captcha;
}
