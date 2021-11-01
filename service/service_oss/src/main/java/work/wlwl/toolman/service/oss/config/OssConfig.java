package work.wlwl.toolman.service.oss.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
//注意prefix要写到最后一个 "." 符号之前
@ConfigurationProperties(prefix = "aliyun.oss")
public class OssConfig {
    private String endpoint;
    private String keyid;
    private String keysecret;
    private String bucketname;
}
