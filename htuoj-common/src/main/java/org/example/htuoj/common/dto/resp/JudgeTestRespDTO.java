package org.example.htuoj.common.dto.resp;

import lombok.Data;
import org.example.htuoj.common.dao.JudgeResult;

import java.util.List;

@Data
public class JudgeTestRespDTO {
    private List<JudgeResult> resultList;
}
