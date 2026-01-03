package org.example.htuoj.common.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ProblemGetByIdRespDTO {
    @ApiModelProperty(value = "主键Id")
    private Long problemId;

    @ApiModelProperty(value = "题目标题")
    private String title;

    @ApiModelProperty(value = "提交次数")
    private Long submitCount;

    @ApiModelProperty(value = "通过次数")
    private Long acceptCount;

    @ApiModelProperty(value = "题目内容")
    private String content;

    @ApiModelProperty(value = "输入格式")
    private String inputDescription;

    @ApiModelProperty(value = "输出描述")
    private String outputDescription;

    @ApiModelProperty(value = "难度级别 0:简单 1:中等 2:困难")
    private Integer difficultyLevel;

    @ApiModelProperty(value = "时间限制")
    private Integer timeLimit;

    @ApiModelProperty(value = "内存限制")
    private Integer memoryLimit;

    @ApiModelProperty(value = "是否通过")
    private Boolean isAccepted;

    @ApiModelProperty(value = "是否收藏")
    private Boolean isCollected;

    @ApiModelProperty(value = "样例集合")
    private List<SampleInfoRespSTO> sampleList;

    @ApiModelProperty(value = "标签集合")
    private String labels;

    @ApiModelProperty(value = "来源集合")
    private String sources;

    @ApiModelProperty("提示信息")
    private String note;

}
