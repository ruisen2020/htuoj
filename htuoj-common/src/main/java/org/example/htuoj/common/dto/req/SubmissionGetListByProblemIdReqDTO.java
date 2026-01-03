package org.example.htuoj.common.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.example.htuoj.common.dao.Submission;

@Data
public class SubmissionGetListByProblemIdReqDTO extends Page<Submission> {
    private Long userId;
    private Long problemId;
    private Integer status;
    private Integer language;
}
