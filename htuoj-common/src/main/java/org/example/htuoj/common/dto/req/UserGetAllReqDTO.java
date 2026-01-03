package org.example.htuoj.common.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.example.htuoj.common.dao.User;

@Data
public class UserGetAllReqDTO extends Page<User> {
    private String userName;
    private String number;
    private String orderBy;
    private String schoolId;
}
