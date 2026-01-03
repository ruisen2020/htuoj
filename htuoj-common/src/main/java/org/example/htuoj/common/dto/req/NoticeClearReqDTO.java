package org.example.htuoj.common.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.example.htuoj.common.dao.Notice;

import java.util.List;

@Data
public class NoticeClearReqDTO  {

    private List<Integer> targetTypeList;
}
