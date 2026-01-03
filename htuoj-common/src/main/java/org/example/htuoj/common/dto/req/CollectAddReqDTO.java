package org.example.htuoj.common.dto.req;

import lombok.Data;

@Data
public class CollectAddReqDTO {
    // 目标类型（0：收藏文章、2：收藏题单）
    private Integer targetType;
    private Long targetId;
    private Boolean isCollect;

}
