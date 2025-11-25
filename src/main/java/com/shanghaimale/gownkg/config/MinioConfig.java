package com.shanghaimale.gownkg.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
    /**
     * endpoint: http://localhost:9000
     * access-key: B48GS01YZ9l8rpo4QCDv
     * secret-key: K6SfOvEBhmYtuuJiE8U7ohnoECQk2D3Vhh1Zh2HL
     * bucket-name: grownkg
     */
    @Value("${minio.endpoint}")
    private String endpoint;
    @Value("${minio.access-key}")
    private String accessKey;
    @Value("${minio.secret-key}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}