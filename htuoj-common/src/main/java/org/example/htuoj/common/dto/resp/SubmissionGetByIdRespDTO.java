package org.example.htuoj.common.dto.resp;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class SubmissionGetByIdRespDTO {

    @ApiModelProperty(value = "主键ID")
    private Long submissionId;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "题目ID")
    private Long problemId;

    @ApiModelProperty(value = "题目标题")
    private String problemTitle;

    @ApiModelProperty(value = "运行状态")
    private Integer status;

    @ApiModelProperty(value = "运行时间")
    private Integer time;

    @ApiModelProperty(value = "使用内存")
    private Integer memory;

    @ApiModelProperty(value = "提交代码")
    private String code;

    @ApiModelProperty(value = "提交语言")
    private Integer language;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
