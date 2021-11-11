package work.wlwl.toolman.service.reptile.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient(value = "service-oss")
public interface OssFileService {

    @GetMapping("/api/oss/file/upload")
    String update(@RequestParam("url") String url,@RequestParam("module") String module);
}
