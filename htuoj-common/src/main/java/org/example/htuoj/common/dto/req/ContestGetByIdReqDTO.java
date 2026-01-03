package org.example.htuoj.common.dto.req;

import lombok.Data;

@Data
public class ContestGetByIdReqDTO {
    private Long contestId;
    private Long userId;
}
