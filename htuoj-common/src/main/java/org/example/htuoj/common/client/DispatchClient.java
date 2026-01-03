package org.example.htuoj.common.client;

import org.example.htuoj.common.convention.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient("htuoj-project")
public interface  DispatchClient {
    @GetMapping("/user-preferences/updateDaily")
    Result<Void> updateDaily();


}
