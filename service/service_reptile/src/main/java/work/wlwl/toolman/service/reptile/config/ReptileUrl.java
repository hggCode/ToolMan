package work.wlwl.toolman.service.reptile.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "reptile.url")
@Component
@Data
public class ReptileUrl {
    private String testUrl;
    private String getProductByBrandUrl;
    private String itemUrl;
    private String getBuyCountUrl;
}
