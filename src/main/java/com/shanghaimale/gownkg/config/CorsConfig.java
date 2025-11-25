package com.shanghaimale.gownkg.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 允许接口跨域，使用是需要在接口上单独追加注解
 */

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 允许所有路径
//                        .allowedOrigins("http://localhost:3000") // 允许的前端地址
                        .allowedOrigins("http://gownkg.com") // 本地环境映射指定前端域名
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // 允许的 HTTP 方法
                        .allowedHeaders("*") // 允许的请求头
                        .allowCredentials(true); // 允许携带凭证（如 cookies）
            }
        };
    }
}