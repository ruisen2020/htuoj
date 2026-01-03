package org.example.htuoj.common.dto.req;

import lombok.Data;

@Data
public class TrainingProblemDeleteReqDTO {
    private Long trainingId;
    private Long problemId;
}
