package org.example.htuoj.common.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data

public class SampleInfoRespSTO {

    @ApiModelProperty(value = "样例输入")
    private String sampleInput;

    @ApiModelProperty(value = "样例输出")
    private String sampleOutput;
}
