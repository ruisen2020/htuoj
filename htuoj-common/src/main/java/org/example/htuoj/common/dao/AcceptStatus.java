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
 * 学生提交题目是否通过表
 * </p>
 *
 * @author xiaoxin
 * @since 2024-09-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("accept_status")
@ApiModel(value = "AcceptStatus对象", description = "学生提交题目是否通过表")
public class AcceptStatus extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "题目ID")
    private Long problemId;

    @ApiModelProperty(value = "状态 0:未尝试 1:尝试成功 2:尝试失败")
    private Integer status;


}
