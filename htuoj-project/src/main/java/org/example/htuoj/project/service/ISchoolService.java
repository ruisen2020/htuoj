package org.example.htuoj.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.htuoj.common.dao.School;
import org.example.htuoj.common.dto.req.SchoolGetListReqDTO;
import org.example.htuoj.common.dto.resp.SchoolGetListRespDTO;

import java.util.List;

public interface ISchoolService extends IService<School> {
    List<SchoolGetListRespDTO> getSchoolList(SchoolGetListReqDTO reqDTO);
}
