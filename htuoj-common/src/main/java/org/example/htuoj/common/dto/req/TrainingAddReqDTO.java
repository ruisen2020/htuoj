package org.example.htuoj.common.dto.req;

import lombok.Data;

@Data
public class TrainingAddReqDTO {
    //    private Long userId;
    private Long trainingId;
    private String title;
    private String description;
}
