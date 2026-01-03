package org.example.htuoj.common.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.example.htuoj.common.dao.Problem;

import java.util.List;

@Data
public class ProblemPageReqDTO extends Page<Problem> {
    private Integer difficultyLevel;
    private Integer status;
    private List<String> labels;
    private List<String> sources;
    // 模糊查询
    private String search;
    private Long userId ;
}
