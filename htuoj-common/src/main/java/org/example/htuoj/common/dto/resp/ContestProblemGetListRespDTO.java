package org.example.htuoj.common.dto.resp;

import lombok.Data;

@Data
public class ContestProblemGetListRespDTO {
    private Long problemId;
    private String problemTitle;
    private String displayId;
    private Boolean isAccepted;
    private Integer acceptCount;
    private Integer submitCount;
}
