package work.wlwl.toolman.service.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.wlwl.toolman.service.base.entity.ResultCodeEnum;
import work.wlwl.toolman.service.base.exception.GlobalException;
import work.wlwl.toolman.service.oss.config.OssConfig;
import work.wlwl.toolman.service.oss.service.FileService;
import work.wlwl.toolman.service.oss.utils.WeChetSend;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;


@Service
public class FileServiceImpl implements FileService {


    @Autowired
    private OssConfig OssConfig;

    @Override
    public String upload(InputStream inputStream, String module, String originalFilename) {
        //    读取公共文件
        String bucketName = OssConfig.getBucketName();
        String endpoint = OssConfig.getEndpoint();

        // 创建OSSClient实例。
        OSS ossClient = this.init();
        //判断 bucketName是否存在
        this.doesBucketExist(ossClient, bucketName);

        //构建日期路径：avatar/2021/11/01/文件名
        String folder = new DateTime().toString("yyyy/MM/dd");
        //文件名：uuid.扩展名
        String fileName = UUID.randomUUID().toString();
//        获取后缀名
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
//        完整路径
        String path = module + "/" + folder + "/" + fileName + fileExtension;

        //文件上传至阿里云
        // 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称。
        ossClient.putObject(bucketName, path, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();

        //返回url地址
        return "https://" + bucketName + "." + endpoint + "/" + path;
    }

    @Override
    public String upload(String url, String module) {
        // 创建OSSClient实例。
        OSS ossClient = this.init();
        try {
            //    读取公共文件
            String bucketName = OssConfig.getBucketName();
            String endpoint = OssConfig.getEndpoint();
            //判断 bucketName是否存在
            this.doesBucketExist(ossClient, bucketName);

            //文件名：uuid.扩展名
            String fileName = UUID.randomUUID().toString().replaceAll("-", "");
            String fileExtension = url.substring(url.lastIndexOf("."));

            String path = module + "/" + fileName + fileExtension;


            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);

            // 填写网络流地址。
            InputStream inputStream = conn.getInputStream();

            // 依次填写Bucket名称和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称。
            ossClient.putObject(bucketName, path, inputStream);

            return "https://" + bucketName + "." + endpoint + "/" + path;
        } catch (IOException e) {
            e.printStackTrace();
            WeChetSend.send("嘎，oss又挂了","oss挂了"+e.getMessage());
            throw new GlobalException(ResultCodeEnum.JD_CONNECT_ERROR);
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
    }


    @Override
    public void removeFile(String url) {
        String bucketName = OssConfig.getBucketName();
        String endpoint = OssConfig.getEndpoint();
        OSS ossClient = this.init();
        String host = "https://" + bucketName + "." + endpoint + "/";
        String objectName = url.substring(host.length());
        // 删除文件。
        ossClient.deleteObject(bucketName, objectName);
        // 关闭OSSClient。
        ossClient.shutdown();

    }

    private OSS init() {
        //    读取公共文件
        String endpoint = OssConfig.getEndpoint();
        String keyId = OssConfig.getKeyId();
        String keySecret = OssConfig.getKeySecret();
        // 创建OSSClient实例。
        return new OSSClientBuilder().build(endpoint, keyId, keySecret);
    }

    private void doesBucketExist(OSS ossClient, String bucketName) {
        //判断 bucketName是否存在
        if (!ossClient.doesBucketExist(bucketName)) {
            //创建bucket
            ossClient.createBucket(bucketName);
            //设置oss实例的访问权限：公共读
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        }
    }


}
