package org.example.htuoj.common.dto.req;

import lombok.Data;
import org.example.htuoj.common.dto.resp.SampleGetListByProblemIdRespDTO;

import java.util.List;

@Data
public class SubmissionAddTestReqDTO {
    private Long problemId;
    private String code;
    private Integer language;
    private Long userId;
    private List<SampleGetListByProblemIdRespDTO> sampleList;
}
