package org.example.htuoj.common.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.example.htuoj.common.dao.AwardInfo;

import java.util.List;

@Data
public class AwardInfoGetAllReqDTO extends Page<AwardInfo> {
    private String studentName;
    private String studentNumber;
    private String studentCollege;
    private String competitionName;
    private String trackName;
    private List<String> awardLevel;
    private String awardTime;
    private String orderBy;
    private String sortOrder;
}
