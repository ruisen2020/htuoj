package org.example.htuoj.common.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.example.htuoj.common.dao.Contest;

@Data
public class ContestGetContestListByOtherOJReqDTO extends Page<Contest> {
}
