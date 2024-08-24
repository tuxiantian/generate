package com.tuxt.generate.minio;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class MinioImageUploader {
    String endpoint = "http://127.0.0.1:9000";
    String accessKey = "minioadmin";
    String secretKey = "minioadmin";

    // 创建存储桶名称，如果不存在将会自动创建
    String bucketName = "images";
    private MinioClient minioClient=MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();



    public String uploadImage(byte[] imageData) {
        try {
            InputStream imageDataStream = new java.io.ByteArrayInputStream(imageData);

            // 使用UUID生成唯一的对象名称，防止覆盖同名文件
            String objectName = UUID.randomUUID().toString();

            // 上传对象
            ObjectWriteResponse objectWriteResponse = this.minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(imageDataStream, imageData.length, -1)
                            .contentType("image/jpeg") // 根据图片类型设置正确的Content-Type
                            .build()
            );

            String presignedUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .method(Method.GET)
                            .expiry(1, TimeUnit.DAYS) // 设置链接有效期为1天
                            .build()
            );
            System.out.println("图像上传成功，对象名称：" + objectName);
            return presignedUrl;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}