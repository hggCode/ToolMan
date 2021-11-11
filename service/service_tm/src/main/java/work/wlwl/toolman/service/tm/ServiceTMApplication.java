package work.wlwl.toolman.service.tm;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@ComponentScan({"work.wlwl.toolman"})

public class ServiceTMApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceTMApplication.class, args);
    }
}
