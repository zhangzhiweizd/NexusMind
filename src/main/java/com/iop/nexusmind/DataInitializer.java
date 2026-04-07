package com.iop.nexusmind;

import com.iop.nexusmind.entity.Role;
import com.iop.nexusmind.entity.User;
import com.iop.nexusmind.repository.RoleRepository;
import com.iop.nexusmind.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 数据初始化器 - 用于测试
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 构造函数，注入依赖
     */
    public DataInitializer(UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 应用启动时执行数据初始化
     * 创建默认角色和测试用户账户
     */
    @Override
    public void run(String... args) throws Exception {
        // 创建管理员角色
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
            Role role = new Role();
            role.setName("ROLE_ADMIN");
            role.setDescription("管理员");
            return roleRepository.save(role);
        });

        // 创建普通用户角色
        Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
            Role role = new Role();
            role.setName("ROLE_USER");
            role.setDescription("普通用户");
            return roleRepository.save(role);
        });

        // 创建测试管理员用户
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@nexusmind.com");
            admin.setNickname("管理员");
            admin.setEnabled(true);
            admin.setRoles(Arrays.asList(adminRole, userRole));
            userRepository.save(admin);
            System.out.println("✓ 创建测试管理员账户：admin/admin123");
        }

        // 创建测试普通用户
        if (!userRepository.existsByUsername("user")) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setEmail("user@nexusmind.com");
            user.setNickname("测试用户");
            user.setEnabled(true);
            user.setRoles(Arrays.asList(userRole));
            userRepository.save(user);
            System.out.println("✓ 创建测试普通用户账户：user/user123");
        }

        System.out.println("\n===========================================");
        System.out.println("NexusMind 知识管理系统已启动！");
        System.out.println("访问 Swagger UI: http://localhost:8080/swagger-ui.html");
        System.out.println("===========================================\n");
    }
}
