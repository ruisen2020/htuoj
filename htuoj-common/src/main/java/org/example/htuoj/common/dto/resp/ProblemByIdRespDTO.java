package org.example.htuoj.common.dto.resp;

import lombok.Data;

import java.util.List;

@Data
public class ProblemByIdRespDTO {
    private Long ProblemId;
    private String title;
    private String source;
    private Integer submitCount;
    private Integer acceptCount;
    private List<String> labels;
    private Integer difficultyLevel;
    private Integer status;
}
