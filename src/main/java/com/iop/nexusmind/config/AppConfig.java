package com.iop.nexusmind.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * 应用配置类
 * 配置全局通用的 Bean 组件
 */
@Configuration
public class AppConfig {

    /**
     * 配置ModelMapper对象映射工具
     * 用于Entity和DTO之间的转换
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    /**
     * 配置RestTemplate HTTP客户端
     * 设置连接超时和读取超时时间
     */
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);  // 连接超时 5 秒
        factory.setReadTimeout(30000);    // 读取超时 30 秒
        return new RestTemplate(factory);
    }
}
