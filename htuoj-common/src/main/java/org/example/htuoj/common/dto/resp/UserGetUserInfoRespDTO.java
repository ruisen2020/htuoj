package org.example.htuoj.common.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class UserGetUserInfoRespDTO {
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "真实姓名")
    private String realName;
    @ApiModelProperty(value = "学号/工号")
    private String number;


    @ApiModelProperty(value = "性别")
    private String gender;
    @ApiModelProperty(value = "学校id")
    private Long schoolId;
    @ApiModelProperty(value = "学校")
    private String schoolName;


    @ApiModelProperty(value = "个人简介")
    private String profile;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "通过题目数")
    private Integer acceptCount;

    @ApiModelProperty(value = "最近登录时间")
    private Date lastLoginTime;

    @ApiModelProperty(value = "是否在线")
    private String onlineStatus;

    @ApiModelProperty(value = "是否被我关注")
    private Boolean isFollowedByMe;
}
