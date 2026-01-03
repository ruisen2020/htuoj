package org.example.htuoj.judge.utils;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.example.htuoj.judge.config.OssConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class OssUtil {

    @Autowired
    private OssConfig ossConfig;

    public static void main(String[] args) {

    }


    public  void getZipFile(String objectName) {
        String endpoint = ossConfig.getEndpoint();
        String accessKeyId = ossConfig.getAccessKeyId();
        String accessKeySecret = ossConfig.getAccessKeySecret();
        // 填写Bucket名称，例如examplebucket。
        String bucketName = ossConfig.getBucketName();
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        String localFile = "1.zip";
        try {
            // 下载OSS文件到本地文件
            ossClient.getObject(new com.aliyun.oss.model.GetObjectRequest(bucketName, objectName), new File(localFile));
            // 创建File对象
            File zipFile = new File(localFile);
            System.out.println("文件已成功下载到: " + zipFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭OSSClient
            ossClient.shutdown();
        }
    }
}
