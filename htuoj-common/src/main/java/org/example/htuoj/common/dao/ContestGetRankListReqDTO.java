package org.example.htuoj.common.dao;

import lombok.Data;

import java.util.List;

@Data
public class ContestGetRankListReqDTO {
    private Long contestId;
    private String userName;
    private List<Long> provinceId;
    private List<Long> schoolId;
    private Long userId;
}
