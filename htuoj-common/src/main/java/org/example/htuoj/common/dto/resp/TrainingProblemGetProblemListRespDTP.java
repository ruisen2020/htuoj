package org.example.htuoj.common.dto.resp;

import lombok.Data;

@Data
public class TrainingProblemGetProblemListRespDTP {
    private Long problemId;
    private String title;
    private String sources;
    private Integer submitCount;
    private Integer acceptCount;
    private String labels;
    private Integer difficultyLevel;
    private Integer status;
    private String passRate;
    private Integer order;
}
