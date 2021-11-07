package work.wlwl.toolman.service.reptile.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import work.wlwl.toolman.service.base.entity.R;
import work.wlwl.toolman.service.reptile.feign.fallback.OssFileServiceFallBack;

@Service
@FeignClient(value = "service-oss",fallback = OssFileServiceFallBack.class)
public interface OssFileService {

    @GetMapping("/api/oss/file/upload")
    R update(String url, String module);
}
