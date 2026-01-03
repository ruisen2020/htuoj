package org.example.htuoj.common.dto.req;

import lombok.Data;

@Data
public class SubmissionAddReqDTO {
    private Long problemId;
    private String code;
    private Integer language;
    private Long userId;
    private Long contestId;
}
