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
import java.util.Date;

/**
 * <p>
 * 任务表
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("task")
@ApiModel(value="Task对象", description="任务表")
public class Task extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务主键ID")
    @TableId(value = "task_id", type = IdType.AUTO)
    private Long taskId;

    @ApiModelProperty(value = "创建用户ID")
    private Long userId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "截止时间")
    private Date deadline;

    @ApiModelProperty(value = "是否完成")
    private Boolean isCompleted;



}
