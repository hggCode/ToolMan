package work.wlwl.toolman.service.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.wlwl.toolman.service.oss.config.OssConfig;
import work.wlwl.toolman.service.oss.service.FileService;

import java.io.InputStream;
import java.util.UUID;


@Service
public class FileServiceImpl implements FileService {


    @Autowired
    private OssConfig OssConfig;

    @Override
    public String upload(InputStream inputStream, String module, String originalFilename) {
        //    读取公共文件
        String bucketname = OssConfig.getBucketname();
        String endpoint = OssConfig.getEndpoint();
        String keyid = OssConfig.getKeyid();
        String keysecret = OssConfig.getKeysecret();

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, keyid, keysecret);
        //判断 bucketname是否存在
        if (!ossClient.doesBucketExist(bucketname)) {
            //创建bucket
            ossClient.createBucket(bucketname);
            //设置oss实例的访问权限：公共读
            ossClient.setBucketAcl(bucketname, CannedAccessControlList.PublicRead);
        }
        //构建日期路径：avatar/2019/02/26/文件名
        String folder = new DateTime().toString("yyyy/MM/dd");
        //文件名：uuid.扩展名
        String fileName = UUID.randomUUID().toString();
//        获取后缀名
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
//        完整路径
        String key = module + "/" + folder + "/" + fileName + fileExtension;

        //文件上传至阿里云
        // 依次填写Bucket名称（例如examplebucket）和Object完整路径（例如exampledir/exampleobject.txt）。Object完整路径中不能包含Bucket名称。
        ossClient.putObject(OssConfig.getBucketname(), key, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();

        //返回url地址
        return "https://" + bucketname + "." + endpoint + "/" + key;
    }

    @Override
    public void removeFile(String url) {
        //    读取公共文件
        String bucketname = OssConfig.getBucketname();
        String endpoint = OssConfig.getEndpoint();
        String keyid = OssConfig.getKeyid();
        String keysecret = OssConfig.getKeysecret();


        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, keyid, keysecret);

        String host = "https://" + bucketname + "." + endpoint + "/";
        String objectName = url.substring(host.length());
        System.out.println("url--->"+url);
        System.out.println("host---->"+host);
        System.out.println("objectName--》"+objectName);
        // 删除文件。
        ossClient.deleteObject(bucketname, objectName);

        // 关闭OSSClient。
        ossClient.shutdown();

    }


}
