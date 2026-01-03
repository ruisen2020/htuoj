package org.example.htuoj.common.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.example.htuoj.common.dao.Training;

@Data
public class TrainingGetOfficialTrainingListReqDTO extends Page<Training> {
    private Long userId;
    private Integer type;
}
