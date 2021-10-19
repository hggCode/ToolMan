package work.wlwl.toolman.service.statistics;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan({"work.wlwl.toolman"})
@EnableDiscoveryClient
@EnableFeignClients
//  开启定时任务
@EnableScheduling
public class ServiceStatisticsApplication {
}
