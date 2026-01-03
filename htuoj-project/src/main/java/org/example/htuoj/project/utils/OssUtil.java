package org.example.htuoj.project.utils;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import org.example.htuoj.common.utils.UserHolder;
import org.example.htuoj.common.convention.exception.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.example.htuoj.project.config.OssConfig;
import org.example.htuoj.project.enums.UserErrorCodeEnum;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static org.python.google.common.io.Files.getFileExtension;

@Component
public class OssUtil {

    @Autowired
    private OssConfig ossConfig;

    public static void main(String[] args) {

    }

    public String uploadAvatar(MultipartFile file) {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = ossConfig.getEndpoint();
        String accessKeyId = ossConfig.getAccessKeyId();
        String accessKeySecret = ossConfig.getAccessKeySecret();
        // 填写Bucket名称，例如examplebucket。
        String bucketName = ossConfig.getBucketName();
        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
        // 获取文件原始名称
        String originalFilename = file.getOriginalFilename();
        // 检查文件名是否合法
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new ClientException(UserErrorCodeEnum.USER_UPDATE_AVATAR_ERROR);
        }
        // 获取文件扩展名
        String fileExtension = getFileExtension(originalFilename);

        String number = UserHolder.getUserId().toString();
        String objectName = "htuoj/userAvatar/" + number + "." + fileExtension;
        System.out.println(objectName);
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            byte[] content = file.getBytes();
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content));
            String userAvatarUrl = "https://" + bucketName + "." + endpoint + "/" + objectName;
            return userAvatarUrl;
        } catch (OSSException | ClientException | IOException oe) {
            throw new ClientException(UserErrorCodeEnum.USER_UPDATE_AVATAR_ERROR);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    public String uploadArticlePictureAndFile(MultipartFile file) {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = ossConfig.getEndpoint();
        String accessKeyId = ossConfig.getAccessKeyId();
        String accessKeySecret = ossConfig.getAccessKeySecret();
        // 填写Bucket名称，例如examplebucket。
        String bucketName = ossConfig.getBucketName();
        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
        // 获取文件原始名称
        String originalFilename = file.getOriginalFilename();
        // 检查文件名是否合法
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new ClientException(UserErrorCodeEnum.USER_UPDATE_AVATAR_ERROR);
        }
        // 获取文件扩展名
        String fileExtension = getFileExtension(originalFilename);

        UUID uuid = UUID.randomUUID();
        // 随机生成文章编号
        String objectName = "htuoj/article/" + uuid + "." + fileExtension;
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            byte[] content = file.getBytes();
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content));
            String userAvatarUrl = "https://" + bucketName + "." + endpoint + "/" + objectName;
            return userAvatarUrl;
        } catch (OSSException | ClientException | IOException oe) {
            throw new ClientException(UserErrorCodeEnum.USER_UPDATE_AVATAR_ERROR);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    public String uploadArticleCover(MultipartFile file) {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = ossConfig.getEndpoint();
        String accessKeyId = ossConfig.getAccessKeyId();
        String accessKeySecret = ossConfig.getAccessKeySecret();
        // 填写Bucket名称，例如examplebucket。
        String bucketName = ossConfig.getBucketName();
        // 填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称。
        // 获取文件原始名称
        String originalFilename = file.getOriginalFilename();
        // 检查文件名是否合法
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new ClientException(UserErrorCodeEnum.USER_UPDATE_AVATAR_ERROR);
        }
        // 获取文件扩展名
        String fileExtension = getFileExtension(originalFilename);

        UUID uuid = UUID.randomUUID();
        // 随机生成文章编号
        String objectName = "htuoj/article/" + UserHolder.getUserId() + ":" + uuid + "." + fileExtension;
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            byte[] content = file.getBytes();
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content));
            String userAvatarUrl = "https://" + bucketName + "." + endpoint + "/" + objectName;
            return userAvatarUrl;
        } catch (OSSException | ClientException | IOException oe) {
            throw new ClientException(UserErrorCodeEnum.USER_UPDATE_AVATAR_ERROR);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    public void getZipFile(String objectName) {
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
