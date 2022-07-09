package com.rzk.utils;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @PackageName : com.rzk.utils
 * @FileName : MinIoClientConfig
 * @Description :
 * @Author : rzk
 * @CreateTime : 7/6/2022 下午9:36
 * @Version : v1.0
 */
@Data
@Component
public class MinIoClientConfig {
    //minio地址
    @Value("${minio.endpoint}")
    private String endpoint;
    //minio生成访问密钥
    @Value("${minio.accessKey}")
    private String accessKey;
    //密钥
    @Value("${minio.secretKey}")
    private String secretKey;

    /**
     * 注入minio 客户端
     * @return
     */
    @Bean
    public MinioClient minioClient(){
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

}
