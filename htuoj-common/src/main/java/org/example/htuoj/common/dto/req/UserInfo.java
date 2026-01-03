package org.example.htuoj.common.dto.req;

import lombok.Data;

@Data
public class UserInfo {
    private String profile;

    private String userName;

    private String realName;

    private Long id;

    private Long schoolId;
}
