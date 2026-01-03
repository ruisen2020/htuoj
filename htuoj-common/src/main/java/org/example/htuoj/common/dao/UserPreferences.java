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
 * 用户偏好表
 * </p>
 *
 * @author xiaoxin
 * @since 2024-10-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_preferences")
@ApiModel(value = "UserPreferences对象", description = "用户偏好表")
public class UserPreferences extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户偏好表主键ID")
    @TableId(value = "user_preferences_id", type = IdType.AUTO)
    private Long userPreferencesId;

    @ApiModelProperty(value = "用户ID")
    private Long userId;
    @ApiModelProperty(value = "通过总数")
    private Integer acceptCount;
    @ApiModelProperty(value = "通过简单题总数")
    private Integer acceptEasyCount;

    @ApiModelProperty(value = "通过中等题总数")
    private Integer acceptMediumCount;

    @ApiModelProperty(value = "通过困难题总数")
    private Integer acceptHardCount;

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

    @ApiModelProperty(value = "coderforces通过题数")
    private Integer codeforcesAcceptCount;

    @ApiModelProperty(value = "牛客通过题数")
    private Integer nowcoderAcceptCount;

    @ApiModelProperty(value = "leetcode通过题数")
    private Integer leetcodeAcceptCount;

    @ApiModelProperty(value = "luogu通过题数")
    private Integer luoguAcceptCount;

    @ApiModelProperty(value = " atcoder通过题数")
    private Integer  atcoderAcceptCount;

    @ApiModelProperty(value = "acwing通过题数")
    private Integer acwingAcceptCount;

    @ApiModelProperty(value = "codeforces排名")
    private Integer codeforcesRating;

    @ApiModelProperty(value = "牛客排名")
    private Integer nowcoderRating;

    @ApiModelProperty(value = "leetcode排名")
    private Integer leetcodeRating;

    @ApiModelProperty(value = "洛谷排名")
    private Integer luoguRating;

    @ApiModelProperty(value = "acwing排名")
    private Integer acwingRating;

    @ApiModelProperty(value = "atcoder排名")
    private Integer atcoderRating;

    @ApiModelProperty(value = "博客地址")
    private String blog;

    @ApiModelProperty(value = "github仓库地址")
    private String github;


}
