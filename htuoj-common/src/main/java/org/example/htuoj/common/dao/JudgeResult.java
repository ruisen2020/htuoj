package org.example.htuoj.common.dao;

import lombok.Data;

@Data
public class JudgeResult {
    private Integer status;
    private Long time;
    private Long memory;
    private String input;
    private String output;
    private String realOutput;
}
