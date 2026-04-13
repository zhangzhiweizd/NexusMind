package com.iop.nexusmind.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据库注释初始化器
 * 在应用启动完成后，为 PostgreSQL 数据库添加表和字段注释
 * 注意：仅在 ddl-auto 为 update/create 时自动执行注释脚本
 *      当 ddl-auto 为 validate/none 时跳过（表结构已稳定）
 */
@Slf4j
@Component
@Profile("prod")
public class SchemaCommentInitializer {

    private final JdbcTemplate jdbcTemplate;
    private final Environment environment;

    // 需要执行注释脚本的 ddl-auto 模式
    private static final List<String> DDL_MODES_NEED_COMMENTS = Arrays.asList("update", "create", "create-drop");

    public SchemaCommentInitializer(JdbcTemplate jdbcTemplate, Environment environment) {
        this.jdbcTemplate = jdbcTemplate;
        this.environment = environment;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initComments() {
        // 获取 ddl-auto 配置
        String ddlAuto = environment.getProperty("spring.jpa.hibernate.ddl-auto", "none");
        
        // 只有在 update/create 模式下才执行注释脚本
        if (!DDL_MODES_NEED_COMMENTS.contains(ddlAuto.toLowerCase())) {
            log.info("当前 ddl-auto 模式为: {}，跳过注释初始化（表结构已稳定）", ddlAuto);
            return;
        }

        log.info("当前 ddl-auto 模式为: {}，开始初始化数据库表注释...", ddlAuto);
        
        try {
            // 读取 SQL 文件
            ClassPathResource resource = new ClassPathResource("db/comment-init.sql");
            String sql;
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                sql = reader.lines().collect(Collectors.joining("\n"));
            }

            // 分割并执行 SQL 语句
            String[] statements = sql.split(";");
            int successCount = 0;
            int skipCount = 0;
            int errorCount = 0;

            for (String statement : statements) {
                String trimmed = statement.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("--")) {
                    continue;
                }

                try {
                    jdbcTemplate.execute(trimmed);
                    successCount++;
                } catch (Exception e) {
                    // PostgreSQL 的 COMMENT ON 在对象不存在时会报错
                    // 其他情况（包括注释已存在）会直接覆盖，不会报错
                    String errorMsg = e.getMessage();
                    if (errorMsg != null && errorMsg.contains("does not exist")) {
                        skipCount++;
                        log.warn("跳过注释（表或字段不存在）: {}", trimmed.substring(0, Math.min(60, trimmed.length())));
                    } else {
                        errorCount++;
                        log.error("添加注释失败: {}", trimmed.substring(0, Math.min(60, trimmed.length())), e);
                    }
                }
            }

            log.info("数据库注释初始化完成！成功: {} 条，跳过: {} 条，失败: {} 条", 
                    successCount, skipCount, errorCount);
            
        } catch (Exception e) {
            log.error("数据库注释初始化失败", e);
        }
    }
}
