package org.example.htuoj.common.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.example.htuoj.common.dao.User;

@Data
public class UserGetUserTopListReqDTO  extends Page<User> {
}
