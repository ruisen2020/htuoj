package org.example.htuoj.project.controller;

import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.convention.result.Results;
import org.example.htuoj.common.dto.req.SchoolGetListReqDTO;
import org.example.htuoj.common.dto.resp.SchoolGetListRespDTO;
import org.example.htuoj.project.service.ISchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/school")
public class SchoolController {

    @Autowired
    private ISchoolService schoolService;

    @PostMapping("getSchoolList")
    public Result<List<SchoolGetListRespDTO>> getSchoolList(@RequestBody SchoolGetListReqDTO reqDTO) {
        List<SchoolGetListRespDTO> schoolGetListRespDTOList = schoolService.getSchoolList(reqDTO);
        return Results.success(schoolGetListRespDTOList);
    }
}
