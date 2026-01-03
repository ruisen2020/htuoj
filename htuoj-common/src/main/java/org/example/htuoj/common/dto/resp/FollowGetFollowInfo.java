package org.example.htuoj.common.dto.resp;

import lombok.Data;

import java.util.List;

@Data
public class FollowGetFollowInfo {
    private List<UserGetAllRespDTO> followList;
    private List<UserGetAllRespDTO> fansList;
}
