package org.example.htuoj.project.controller;


import org.example.htuoj.project.service.IAcceptStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.htuoj.project.service.IAcceptStatusService;

/**
 * <p>
 * 学生提交题目是否通过表 前端控制器
 * </p>
 *
 * @author xiaoxin
 * @since 2024-09-17
 */
@RestController
@RequestMapping("/accept-status")
public class AcceptStatusController {
    @Autowired
    private IAcceptStatusService acceptStatusService;


}
