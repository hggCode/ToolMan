package work.wlwl.toolman.service.reptile.feign.fallback;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import work.wlwl.toolman.service.reptile.feign.OssFileService;

@Service
@Slf4j
public class OssFileServiceFallBack implements OssFileService {

    @Override
    public String update(String url, String module) {
        log.error("嘎，上传失败,启动熔断");
        return"嘎，上传失败，启动熔断";
    }
}
