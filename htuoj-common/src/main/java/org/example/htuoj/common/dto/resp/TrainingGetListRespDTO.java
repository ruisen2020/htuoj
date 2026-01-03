package org.example.htuoj.common.dto.resp;

import lombok.Data;

@Data
public class TrainingGetListRespDTO {
    private Long trainingId;

    private String title;
    private String description;

    private Integer collectCount;
    private Integer problemCount;

    private Boolean isCollect;


}
