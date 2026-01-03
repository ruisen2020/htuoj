package org.example.htuoj.common.dto.req;

import lombok.Data;

import java.util.List;

@Data
public class ProblemPageReqCopyDTO  {
    private Integer difficultyLevel;
    private Integer status;
    private List<String> labels;
    private List<String> sources;
    // 模糊查询
    private String search;
    private Long userId = 1L;
}
