package org.example.htuoj.common.dto.req;

import lombok.Data;

import java.util.List;

@Data
public class ProblemGetLabelsAndSourcesReqDTO {
    List<Long> problemIds;
}
