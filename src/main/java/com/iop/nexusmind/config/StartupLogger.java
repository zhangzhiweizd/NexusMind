package com.iop.nexusmind.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartupLogger implements CommandLineRunner {

    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${spring.h2.console.path:/h2-console}")
    private String h2ConsolePath;

    @Override
    public void run(String... args) {
        String banner = """
                
                ╔══════════════════════════════════════════════════════════╗
                ║                                                          ║
                ║           🚀 NexusMind 知识管理系统启动成功！             ║
                ║                                                          ║
                ╠══════════════════════════════════════════════════════════╣
                                                                          ║
                ║  📖 API 文档 (Swagger UI):                               ║
                ║     👉 http://localhost:%s/swagger-ui.html            ║
                ║                                                          ║
                ║  📄 API 文档 (JSON):                                     ║
                ║     👉 http://localhost:%s/v3/api-docs                ║
                ║                                                          ║
                ║  💾 H2 数据库控制台：                                    ║
                ║     👉 http://localhost:%s%s                         ║
                ║     JDBC URL: jdbc:h2:mem:nexusmind_db                  ║
                ║     用户名：sa                                          ║
                ║     密码：(空)                                           ║
                ║                                                          ║
                ╠══════════════════════════════════════════════════════════╣
                ║  💡 提示：                                                
                ║  - 在 Swagger UI 中可以直接测试所有 API 接口              ║
                ║  - 使用 H2 控制台可以查看数据库数据                        ║
                ╚══════════════════════════════════════════════════════════╝
                """.formatted(serverPort, serverPort, serverPort, h2ConsolePath);

        log.info(banner);
    }
}
