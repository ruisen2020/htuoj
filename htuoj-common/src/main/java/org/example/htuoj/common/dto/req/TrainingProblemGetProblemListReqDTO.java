package org.example.htuoj.common.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.example.htuoj.common.dao.TrainingProblem;

@Data
public class TrainingProblemGetProblemListReqDTO extends Page<TrainingProblem> {
    private Long trainingId;
    private Long userId;
}
