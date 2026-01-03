package org.example.htuoj.common.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserPreferencesInfo {
    @ApiModelProperty(value = "codeforces用户名")
    private String codeforcesUserName;

    @ApiModelProperty(value = "牛客用户ID")
    private String nowcoderUserId;

    @ApiModelProperty(value = "力扣用户名")
    private String leetcodeUserName;

    @ApiModelProperty(value = "洛谷用户ID")
    private String luoguUserId;

    @ApiModelProperty(value = "acwing用户ID")
    private String acwingUserId;

    @ApiModelProperty(value = " atcoder用户名")
    private String atcoderUserName;

    @ApiModelProperty(value = "博客地址")
    private String blog;

    @ApiModelProperty(value = "github仓库地址")
    private String github;
}
