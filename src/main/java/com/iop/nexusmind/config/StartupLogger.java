package com.iop.nexusmind.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupLogger implements CommandLineRunner {

    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${spring.h2.console.path:/h2-console}")
    private String h2ConsolePath;



    @Override
    public void run(String... args) {

        
        StringBuilder banner = new StringBuilder();
        banner.append("\n");
        banner.append("╔══════════════════════════════════════════════════════════╗\n");
        banner.append("║                                                          ║\n");
        banner.append("║           🚀 NexusMind 知识管理系统启动成功！             ║\n");
        banner.append("║                                                          ║\n");
        banner.append("╠══════════════════════════════════════════════════════════╣\n");
        banner.append("║                                                          ║\n");
        banner.append("║  📖 API 文档 (Swagger UI):                               ║\n");
        banner.append(String.format("║     👉 http://localhost:%s%s                           ║\n", serverPort, "/swagger-ui.html"));
        banner.append("║                                                          ║\n");
        banner.append("║  📄 API 文档 (JSON):                                     ║\n");
        banner.append(String.format("║     👉 http://localhost:%s%s                          ║\n", serverPort, "/v3/api-docs"));
        banner.append("║                                                          ║\n");
        banner.append("║  💾 H2 数据库控制台：                                    ║\n");
        banner.append(String.format("║     👉 http://localhost:%s%s                          ║\n", serverPort, h2ConsolePath));
        banner.append("║     JDBC URL: jdbc:h2:mem:nexusmind_db                  ║\n");
        banner.append("║     用户名：sa                                          ║\n");
        banner.append("║     密码：(空)                                           ║\n");
        banner.append("║                                                          ║\n");
        banner.append("║                                                          ║\n");
        banner.append("╠══════════════════════════════════════════════════════════╣\n");
        banner.append("║  💡 提示：                                                ║\n");
        banner.append("║  - 在 Swagger UI 中可以直接测试所有 API 接口              ║\n");
        banner.append("║  - 使用 H2 控制台可以查看数据库数据                        ║\n");
        banner.append("╚══════════════════════════════════════════════════════════╝\n");
        
        log.info(banner.toString());
    }
}
