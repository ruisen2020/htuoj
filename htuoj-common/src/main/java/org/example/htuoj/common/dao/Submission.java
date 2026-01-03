package org.example.htuoj.common.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户提交记录表
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("submission")
@ApiModel(value = "Submission对象", description = "用户提交记录表")
public class Submission extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "submission_id", type = IdType.AUTO)
    private Long submissionId;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "题目ID")
    private Long problemId;


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

    @ApiModelProperty(value = "比赛ID")
    private Long contestId;

}
