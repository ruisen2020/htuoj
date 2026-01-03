package org.example.htuoj.dispatch.xxl.job;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import org.example.htuoj.common.convention.result.Result;
import org.example.htuoj.common.client.DispatchClient;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;


@Component
@RequiredArgsConstructor
public class DispatchJobHandler {


    private final RestTemplate restTemplate;

//    private final DiscoveryClient discoveryClient;

    private final DispatchClient dispatchClient;

    @XxlJob("firstJobHandler")
    public void testJobHandler() {
        XxlJobHelper.log("XXL-JOB, Hello World");
        // 这里远程调用project服务，启动爬虫程序
//        Result result = restTemplate.getForObject("http://localhost:9001/user-preferences/updateDaily", Result.class);
//
//        List<ServiceInstance> instances = discoveryClient.getInstances("htuoj-project");
//
//        if (instances.size() > 0) {
//            ServiceInstance serviceInstance = instances.get(RandomUtil.randomInt(instances.size()));
//            String url = serviceInstance.getUri().toString() + "/user-preferences/updateDaily";
//            Result result = restTemplate.getForObject(url, Result.class);
//            if (result != null && Objects.equals(result.getCode(), "200")) {
//                XxlJobHelper.handleSuccess("更新成功");
//            } else {
//                XxlJobHelper.handleFail("更新失败");
//            }
//        }
        Result<Void> result = dispatchClient.updateDaily();

        if (result != null && Objects.equals(result.getCode(), "200")) {
            XxlJobHelper.handleSuccess("更新成功");
        } else {
            XxlJobHelper.handleFail("更新失败");
        }
    }
}
