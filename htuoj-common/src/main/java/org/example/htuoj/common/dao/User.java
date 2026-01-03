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
 * 用户表
 * </p>
 *
 * @author xiaoxin
 * @since 2024-09-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
@ApiModel(value = "User对象", description = "用户表")
public class User extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户昵称")
    private String userName;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "学号/工号")
    private String number;

    @ApiModelProperty(value = "学号/工号")
    private Date lastLoginTime;

    @ApiModelProperty(value = "性别")
    private String gender;

    @ApiModelProperty(value = "状态（0：未审核 1：审核成功 2：审核失败）")
    private Integer state;

    @ApiModelProperty(value = "学校ID")
    private Long schoolId;

    @ApiModelProperty(value = "邮箱")
    private String mail;

    @ApiModelProperty(value = "个人简介")
    private String profile;

    @ApiModelProperty(value = "头像")
    private String avatar;


}
