package org.example.htuoj.common.dto.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserPreferencesGetRatingInfo {

    @ApiModelProperty(value = "通过简单题总数")
    private Integer acceptEasyCount;

    @ApiModelProperty(value = "通过中等题总数")
    private Integer acceptMediumCount;

    @ApiModelProperty(value = "通过困难题总数")
    private Integer acceptHardCount;

    @ApiModelProperty(value = "codeforces用户名")
    private String codeforcesUserName;

    @ApiModelProperty(value = "牛客用户ID")
    private String nowcoderId;

    @ApiModelProperty(value = "力扣用户名")
    private String leetcodeUserName;

    @ApiModelProperty(value = "洛谷用户ID")
    private String luoguUserId;

    @ApiModelProperty(value = "acwing用户ID")
    private String acwingUserId;

    @ApiModelProperty(value = "Actor用户名")
    private String actorUserName;

    @ApiModelProperty(value = "coderforces通过题数")
    private Integer coderforcesAcceptCount;

    @ApiModelProperty(value = "牛客通过题数")
    private Integer nowcoderAcceptCount;

    @ApiModelProperty(value = "leetcode通过题数")
    private Integer leetcodeAcceptCount;

    @ApiModelProperty(value = "luogu通过题数")
    private Integer luoguAcceptCount;

    @ApiModelProperty(value = "actor通过题数")
    private Integer actorAcceptCount;

    @ApiModelProperty(value = "acwing通过题数")
    private Integer acwingAcceptCount;

    @ApiModelProperty(value = "codeforces排名")
    private String codeforcesRating;

    @ApiModelProperty(value = "牛客排名")
    private String nowcoderRating;

    @ApiModelProperty(value = "leetcode排名")
    private String leetcodeRating;

    @ApiModelProperty(value = "洛谷排名")
    private String luoguRating;

    @ApiModelProperty(value = "acwing排名")
    private String acwingRating;

    @ApiModelProperty(value = "atcoder排名")
    private String atcoderRating;

    @ApiModelProperty(value = "博客地址")
    private String blog;

    @ApiModelProperty(value = "github仓库地址")
    private String github;





}
