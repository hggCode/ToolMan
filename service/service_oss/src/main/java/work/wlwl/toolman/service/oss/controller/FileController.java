package work.wlwl.toolman.service.oss.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import work.wlwl.toolman.service.base.entity.R;
import work.wlwl.toolman.service.base.entity.ResultCodeEnum;
import work.wlwl.toolman.service.base.exception.GlobalException;
import work.wlwl.toolman.service.oss.service.FileService;

import java.io.IOException;
import java.io.InputStream;

@Api(tags = "阿里云文件管理")
@RestController
@RequestMapping("/api/oss/file")
@Slf4j
public class FileController {

    @Autowired
    private FileService fileService;

    @ApiOperation("文件上传")
    @PostMapping("upload")
    public R upload(
            @ApiParam(value = "file", required = true)
            @RequestParam("file") MultipartFile file,

            @ApiParam(value = "模块", required = true)
            @RequestParam("module") String module) throws IOException {


        try {
            InputStream inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();

            String upload = fileService.upload(inputStream, module, originalFilename);

            return R.ok().message("上传成功").data("url", upload);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResultCodeEnum.FILE_UPLOAD_ERROR);
        }
    }

    @ApiOperation("文件删除")
    @DeleteMapping("remove")
    public R removerFile(
            @ApiParam("要删除的文件的的url")
            @RequestBody String url) {
        fileService.removeFile(url);
        return R.ok().message("删除成功");

    }
}
