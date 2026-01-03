package org.example.htuoj.common.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.example.htuoj.common.dao.Submission;

@Data
public class SubmissionGetListReqDTO extends Page<Submission> {
    private Long userId;
    private String search;
    private Integer status;
    private Integer language;
    private Long contestId;
    private Long problemId;

}
