package com.iop.nexusmind.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupLogger implements CommandLineRunner {

    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${spring.h2.console.path:/h2-console}")
    private String h2ConsolePath;

    @Value("${spring.h2.console.enabled:false}")
    private boolean h2ConsoleEnabled;

    @Value("${springdoc.swagger-ui.enabled:false}")
    private boolean swaggerEnabled;

    private final Environment environment;

    /**
     * 应用启动后执行，打印系统访问信息
     */
    @Override
    public void run(String... args) {
        // 获取当前激活的环境
        String[] activeProfiles = environment.getActiveProfiles();
        String profile = activeProfiles.length > 0 ? activeProfiles[0] : "default";
        
        StringBuilder banner = new StringBuilder();
        banner.append("\n");
        banner.append("╔══════════════════════════════════════════════════════════╗\n");
        banner.append("║                                                          ║\n");
        banner.append("║           🚀 NexusMind 知识管理系统启动成功！             ║\n");
        banner.append(String.format("║           当前环境: %-36s ║\n", profile.toUpperCase()));
        banner.append("║                                                          ║\n");
        banner.append("╠══════════════════════════════════════════════════════════╣\n");
        banner.append("║                                                          ║\n");
        
        // 根据环境显示不同的访问信息
        if (swaggerEnabled) {
            banner.append("║  📖 API 文档 (Swagger UI):                               ║\n");
            banner.append(String.format("║     👉 http://localhost:%s%s                           ║\n", serverPort, "/swagger-ui.html"));
            banner.append("║                                                          ║\n");
            banner.append("║  📄 API 文档 (JSON):                                     ║\n");
            banner.append(String.format("║     👉 http://localhost:%s%s                          ║\n", serverPort, "/v3/api-docs"));
            banner.append("║                                                          ║\n");
        }
        
        if (h2ConsoleEnabled) {
            banner.append("║  💾 H2 数据库控制台：                                    ║\n");
            banner.append(String.format("║     👉 http://localhost:%s%s                          ║\n", serverPort, h2ConsolePath));
            banner.append("║     JDBC URL: jdbc:h2:mem:nexusmind_db                  ║\n");
            banner.append("║     用户名：sa                                          ║\n");
            banner.append("║     密码：(空)                                           ║\n");
            banner.append("║                                                          ║\n");
        } else {
            banner.append("║  💾 数据库：PostgreSQL                                      ║\n");
            banner.append("║                                                          ║\n");
        }
        
        banner.append("╠══════════════════════════════════════════════════════════╣\n");
        banner.append("║  💡 提示：                                                ║\n");
        if (swaggerEnabled) {
            banner.append("║  - 在 Swagger UI 中可以直接测试所有 API 接口              ║\n");
        }
        if (h2ConsoleEnabled) {
            banner.append("║  - 使用 H2 控制台可以查看数据库数据                        ║\n");
        } else {
            banner.append("║  - 生产环境请使用专业数据库工具连接 PostgreSQL              ║\n");
        }
        banner.append("╚══════════════════════════════════════════════════════════╝\n");
        
        log.info(banner.toString());
    }
}
