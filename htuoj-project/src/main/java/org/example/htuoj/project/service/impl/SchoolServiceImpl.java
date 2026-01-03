package org.example.htuoj.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.htuoj.common.dao.School;
import org.example.htuoj.common.dto.req.SchoolGetListReqDTO;
import org.example.htuoj.common.dto.resp.SchoolGetListRespDTO;
import org.example.htuoj.common.mapper.SchoolMapper;
import org.example.htuoj.project.service.ISchoolService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School> implements ISchoolService {

    @Autowired
    private SchoolMapper schoolMapper;

    @Override
    public List<SchoolGetListRespDTO> getSchoolList(SchoolGetListReqDTO reqDTO) {
        LambdaQueryWrapper<School> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(School::getSchoolName, reqDTO.getSchoolName());
        wrapper.eq(School::getDelFlag, 0);
        List<School> schools = schoolMapper.selectList(wrapper);
        ArrayList<SchoolGetListRespDTO> result = new ArrayList<>();
        for (School school : schools) {
            SchoolGetListRespDTO schoolGetListRespDTO = new SchoolGetListRespDTO();
            schoolGetListRespDTO.setSchoolId(school.getSchoolId());
            schoolGetListRespDTO.setSchoolName(school.getSchoolName());
            result.add(schoolGetListRespDTO);
        }
        return result;
    }
}
