package org.example.htuoj.common.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserGetUserTopListRespDTO {
    @ApiModelProperty(value = "主键ID")
    private Long userId;
    @ApiModelProperty(value = "用户姓名")
    private String userName;
    @ApiModelProperty(value = "头像")
    private String avatar;
    @ApiModelProperty(value = "通过题目数")
    private Integer acceptCount;

}
