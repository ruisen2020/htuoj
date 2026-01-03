package org.example.htuoj.common.dto.req;


import lombok.Data;

@Data
public class UserUpdateAllInfoReqDTO {
    private UserPreferencesInfo userPreferencesInfo;

    private UserInfo userInfo;
}
