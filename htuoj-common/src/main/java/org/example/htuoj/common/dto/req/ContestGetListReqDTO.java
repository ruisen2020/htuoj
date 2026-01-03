package org.example.htuoj.common.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.example.htuoj.common.dao.Contest;

import java.time.LocalDateTime;

@Data
public class ContestGetListReqDTO extends Page<Contest> {
    private String title;
    private Long userId;
}
