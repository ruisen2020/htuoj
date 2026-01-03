package org.example.htuoj.common.dto.resp;

import lombok.Data;
import org.example.htuoj.common.dao.PassProblemDetail;

import java.util.List;

@Data
public class ContestGetRankListRespDTO {
    private Integer rank;
    private Long userId;
    private String number;
    private String userName;
    private String avatar;
    private String schoolName;
    private Integer acceptSum;
    private Integer penaltyTimeSum;
    private List<PassProblemDetail> passList;
}
