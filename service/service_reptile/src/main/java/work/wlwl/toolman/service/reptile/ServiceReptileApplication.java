package work.wlwl.toolman.service.reptile;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"work.wlwl.toolman"})
@EnableDiscoveryClient
@EnableFeignClients
public class ServiceReptileApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceReptileApplication.class, args);
    }
}
