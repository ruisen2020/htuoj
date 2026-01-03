package org.example.htuoj.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.htuoj.common.dao.ContestRegister;
import org.example.htuoj.common.dto.req.ContestRegisterAddReqDTO;

public interface IContestRegisterService extends IService<ContestRegister> {
    void addRegister(ContestRegisterAddReqDTO reqDTO);

    void checkRegister(Long contestId);
}
