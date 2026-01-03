package org.example.htuoj.common.dto.resp;

import lombok.Data;

@Data
public class UserGetAllRespDTO {
    private Long userId;
    private String number;
    private String userName;
    private String avatar;
    private String profile;
    private Boolean isFollowingMe;
    private Boolean isFollowedByMe;
}
