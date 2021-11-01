package work.wlwl.toolman.service.oss.service;

import java.io.InputStream;

public interface FileService {

    /**
     * 文件上传至阿里云
     * @param inputStream 输入流
     * @param module  模块名
     * @param originalFilename 源文件名
     * @return 文件路径
     */
    String upload(InputStream inputStream, String module, String originalFilename);

    /**
     *  删除文件
     * @param url
     */
    void removeFile(String url);
}
