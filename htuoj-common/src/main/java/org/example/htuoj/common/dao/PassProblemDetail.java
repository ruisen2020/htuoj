package org.example.htuoj.common.dao;

import lombok.Data;

@Data
public class PassProblemDetail {
    private String displayId;
    private Long problemId;
    private Boolean isAccept;
    private Integer waSum;
    private Integer penaltyTime;
    private Long submissionId;
}
