package org.example.htuoj.common.dto.resp;

import lombok.Data;

@Data
public class TrainingGetOfficialTrainingListRespDTO {
    private Long userId;
    private String userName;
    private Long trainingId;

    private String title;

    private String description;

    private Integer collectCount;

    private Integer problemCount;

    private Integer completionRate;

    private Integer easyCount;

    private Integer mediumCount;
    private Integer hardCount;

    private Boolean isCollect;
}
