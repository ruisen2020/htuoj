package org.example.htuoj.common.dto.req;

import lombok.Data;

import java.util.Date;

@Data
public class LikeAddReqDTO {
    private Integer targetType;
    private Long targetId;

}
