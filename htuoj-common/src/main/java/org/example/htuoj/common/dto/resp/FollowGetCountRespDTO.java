package org.example.htuoj.common.dto.resp;

import lombok.Data;

@Data
public class FollowGetCountRespDTO {
    // 粉丝数
    private Long fanCount;
    // 关注数
    private Long followCount;
}
