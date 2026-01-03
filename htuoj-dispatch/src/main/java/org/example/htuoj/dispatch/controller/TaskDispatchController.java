package org.example.htuoj.dispatch.controller;

import org.example.htuoj.common.utils.UserHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dispatch")
public class TaskDispatchController {

    @GetMapping("/test")
    public String test() {
        System.out.println(UserHolder.getUserId());
        return String.valueOf(UserHolder.getUserId());
    }
}
