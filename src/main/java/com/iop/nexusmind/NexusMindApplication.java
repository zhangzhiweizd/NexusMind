package com.iop.nexusmind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * NexusMind知识管理系统主应用类
 * 启动Spring Boot应用程序
 */
@SpringBootApplication
@EnableJpaAuditing
public class NexusMindApplication {

    /**
     * 应用程序入口
     */
    public static void main(String[] args) {
        SpringApplication.run(NexusMindApplication.class, args);
    }
}
