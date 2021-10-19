package work.wlwl.toolman.service.oss.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class test {
    @Value("${aliyun.oss.keyid}")
//    @Value("${test}")
    String name;


    @GetMapping("test")
    String test() {
        return name;
    }
}
