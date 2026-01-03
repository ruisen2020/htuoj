package org.example.htuoj.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.htuoj.common.dao.AwardInfo;
import org.example.htuoj.common.dto.req.AwardInfoGetAllReqDTO;
import org.example.htuoj.common.dto.resp.AwardInfoGetAllRespDTO;
import org.example.htuoj.common.dto.resp.AwardInfoGetByUserIDRespDTO;
import org.example.htuoj.common.mapper.AwardInfoMapper;
import org.example.htuoj.project.service.IAwardInfoService;
import org.example.htuoj.project.service.IUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 获奖信息 服务实现类
 * </p>
 *
 * @author xiaoxin
 * @since 2024-09-20
 */
@Service
public class AwardInfoServiceImpl extends ServiceImpl<AwardInfoMapper, AwardInfo> implements IAwardInfoService {
    @Autowired
    private AwardInfoMapper awardInfoMapper;
    @Autowired
    private IUserService userService;

    @Override
    public IPage<AwardInfoGetAllRespDTO> getAll(AwardInfoGetAllReqDTO reqDTO) {
        IPage<AwardInfoGetAllRespDTO> allList = awardInfoMapper.getAllList2(reqDTO);
        return allList;

//        List<AwardInfoGetAllRespDTO> allList = awardInfoMapper.getAllList(reqDTO);
//        LambdaQueryWrapper<AwardInfo> wrapper = new LambdaQueryWrapper<>();
//        wrapper.like(reqDTO.getStudentName() != null, AwardInfo::getStudentName, reqDTO.getStudentName());
//        wrapper.like(reqDTO.getStudentNumber() != null, AwardInfo::getStudentNumber, reqDTO.getStudentNumber());
//        wrapper.eq(reqDTO.getStudentCollege() != null, AwardInfo::getStudentCollege, reqDTO.getStudentCollege());
//        wrapper.like(reqDTO.getCompetitionName() != null, AwardInfo::getCompetitionName, reqDTO.getCompetitionName());
//        wrapper.like(reqDTO.getTrackName() != null, AwardInfo::getTrackName, reqDTO.getTrackName());
//        wrapper.like(reqDTO.getAwardLevel() != null, AwardInfo::getAwardLevel, reqDTO.getAwardLevel());
//        wrapper.like(reqDTO.getAwardTime() != null, AwardInfo::getAwardTime, reqDTO.getAwardTime());
//        // 查询在不分页的情况下的数据总数
//        Long total = awardInfoMapper.selectCount(wrapper);
//        Result<List<AwardInfoGetAllRespDTO>> listResult = new Result<>();
//        listResult.setData(allList);
//        listResult.setTotal(total);
////        Page<AwardInfo> page = awardInfoMapper.selectPage(reqDTO, wrapper);
//        return listResult;
//        LambdaQueryWrapper<AwardInfo> wrapper = new LambdaQueryWrapper<>();
//        wrapper.like(reqDTO.getStudentName() != null, AwardInfo::getStudentName, reqDTO.getStudentName());
//        wrapper.eq(reqDTO.getStudentNumber() != null, AwardInfo::getStudentNumber, reqDTO.getStudentNumber());
//        wrapper.eq(reqDTO.getStudentCollege() != null, AwardInfo::getStudentCollege, reqDTO.getStudentCollege());
//        wrapper.like(reqDTO.getCompetitionName() != null, AwardInfo::getCompetitionName, reqDTO.getCompetitionName());
//        wrapper.like(reqDTO.getTrackName() != null, AwardInfo::getTrackName, reqDTO.getTrackName());
//        wrapper.like(reqDTO.getAwardLevel() != null, AwardInfo::getAwardLevel, reqDTO.getAwardLevel());
//        wrapper.like(reqDTO.getAwardTime() != null, AwardInfo::getAwardTime, reqDTO.getAwardTime());
//        if (StringUtils.isNotEmpty(reqDTO.getOrderBy())) {
//            String sortOrder = reqDTO.getSortOrder();
//            boolean isAsc = "ascending".equals(sortOrder);
//            if (reqDTO.getOrderBy().equals("awardLevel")) {
//                wrapper.orderBy(true, isAsc, AwardInfo::getAwardLevel);
//            } else if (reqDTO.getOrderBy().equals("awardTime")) {
//                wrapper.orderBy(true, isAsc, AwardInfo::getAwardTime);
//            }
//        } else {
//            wrapper.orderByDesc(AwardInfo::getAwardTime);
//        }


//        System.out.println(page.getTotal()+"aaaaaaaaaaaaaaaa");
//        List<AwardInfoGetAllRespDTO> result = new ArrayList<>();
//        for (AwardInfo awardInfo : page.getRecords()) {
//            AwardInfoGetAllRespDTO awardInfoGetAllRespDTO = new AwardInfoGetAllRespDTO();
//            BeanUtils.copyProperties(awardInfo, awardInfoGetAllRespDTO);
//            result.add(awardInfoGetAllRespDTO);
//        }
//        System.out.println(result);
//        for (AwardInfoGetAllRespDTO awardInfoGetAllRespDTO : result) {
//            Long userId = userService.getUserIdByNumber(awardInfoGetAllRespDTO.getStudentNumber());
//            awardInfoGetAllRespDTO.setUserId(userId);
//            System.out.println(userId);
//        }
//        System.out.println(result);
//        return result;
    }

    @Override
    public List<AwardInfoGetByUserIDRespDTO> getByUserId(String studentNumber) {
        LambdaQueryWrapper<AwardInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AwardInfo::getStudentNumber, studentNumber);
        List<AwardInfo> awardInfos = awardInfoMapper.selectList(wrapper);
        return awardInfos.stream().map(awardInfo -> {
            AwardInfoGetByUserIDRespDTO awardInfoGetByUserIDRespDTO = new AwardInfoGetByUserIDRespDTO();
            BeanUtils.copyProperties(awardInfo, awardInfoGetByUserIDRespDTO);
            return awardInfoGetByUserIDRespDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<String> getTrackNameList() {
        return awardInfoMapper.getTrackNameList();
    }

    @Override
    public List<String> getCompetitionNameList() {
        return awardInfoMapper.getCompetitionNameList();
    }
}
