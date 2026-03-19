package com.iop.nexusmind.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@Configuration
@EnableSpringDataWebSupport(
    pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO
)
public class WebConfig {
    // 这个配置类用于解决 PageImpl 序列化警告
}
