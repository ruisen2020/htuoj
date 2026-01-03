package org.example.htuoj.common.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.Data;
import org.example.htuoj.common.dao.Collect;
@Data
public class CollectGetCollectListByUserIdReqDTO extends Page<Collect> {

    private Long userId;

    private Long myUserId;
}
