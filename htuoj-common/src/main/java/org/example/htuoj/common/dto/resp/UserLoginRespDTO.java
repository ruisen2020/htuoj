package org.example.htuoj.common.dto.resp;

import lombok.Data;

@Data
public class UserLoginRespDTO {
    private String token;
    private UserLoginGetInfoDTO userInfo;
}
