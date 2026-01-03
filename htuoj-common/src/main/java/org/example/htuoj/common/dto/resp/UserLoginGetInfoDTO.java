package org.example.htuoj.common.dto.resp;

import lombok.Data;

@Data
public class UserLoginGetInfoDTO {
    private Long userId;
    private String number;
    private String userName;
    private String avatar;

    private String realName;
}
