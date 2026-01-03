package org.example.htuoj.common.dto.req;



import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ProblemUpdateReqDTO {

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "题目标题")
    private String title;

    @ApiModelProperty(value = "题目标签集合")
    private List<String> labels;

    @ApiModelProperty(value = "题目来源")
    private String source;

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

    @ApiModelProperty(value = "难度级别")
    private Integer difficultyLevel;

    @ApiModelProperty(value = "时间限制")
    private Integer timeLimit;

    @ApiModelProperty(value = "内存限制")
    private Integer memoryLimit;
}
