package org.example.htuoj.common.dto.req;

import lombok.Data;
import org.example.htuoj.common.dao.Submission;
import org.example.htuoj.common.dto.resp.SampleGetListByProblemIdRespDTO;

import java.util.List;

@Data
public class JudgeTestReqDTO {
    private Submission submission;
    private List<SampleGetListByProblemIdRespDTO> sampleList;
}
