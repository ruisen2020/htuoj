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
 * 题目表
 * </p>
 *
 * @author xiaoxin
 * @since 2024-09-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("problem")
@ApiModel(value = "Problem对象", description = "题目表")
public class Problem extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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


    @ApiModelProperty(value = "提示信息")
    private String note;


}
