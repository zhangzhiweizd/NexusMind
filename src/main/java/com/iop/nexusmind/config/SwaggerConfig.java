package com.iop.nexusmind.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${nexusmind.openapi.server-url:http://localhost:8080}")
    private String serverUrl;

    @Bean
    public OpenAPI nexusMindOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("NexusMind API")
                .description("知识管理系统 API 文档 - NexusMind Knowledge Management System")
                .version("1.0.0")
                .contact(new Contact()
                    .name("NexusMind Team")
                    .email("support@nexusmind.com")
                    .url("https://github.com/nexusmind"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")))
            .servers(List.of(
                new Server()
                    .url(serverUrl)
                    .description("本地开发服务器")
            ));
    }
}
