package org.example.htuoj.common.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("htuoj-dispatch")
public interface ProjectClient {
    @GetMapping("/dispatch/test")
    String getUserId();
}
