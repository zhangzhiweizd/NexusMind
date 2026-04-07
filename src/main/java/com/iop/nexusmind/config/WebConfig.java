package com.iop.nexusmind.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

/**
 * Web配置类
 * 配置Spring Data Web支持
 */
@Configuration
@EnableSpringDataWebSupport(
    pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO
)
public class WebConfig {
    // CORS配置已在SecurityConfig中统一处理
}
