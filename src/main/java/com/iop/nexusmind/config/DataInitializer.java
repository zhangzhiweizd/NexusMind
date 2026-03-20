package com.iop.nexusmind.config;

import com.iop.nexusmind.entity.Category;
import com.iop.nexusmind.entity.Tag;
import com.iop.nexusmind.repository.CategoryRepository;
import com.iop.nexusmind.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    @Override
    public void run(String... args) {
        log.info("╔════════════════════════════════════════════╗");
        log.info("║       开始初始化基础数据...                ║");
        log.info("╚════════════════════════════════════════════╝");
        
        // 创建分类
        createCategories();
        
        // 创建标签
        createTags();
        
        // 显示所有数据
        displayAllData();
    }

    private void createCategories() {
        log.info("\n【分类初始化】");
        
        categoryRepository.findByName("技术学习").orElseGet(() -> {
            log.info("  ✓ 创建：技术学习");
            return categoryRepository.save(Category.builder()
                .name("技术学习")
                .description("编程和技术相关笔记")
                .build());
        });

        categoryRepository.findByName("生活随笔").orElseGet(() -> {
            log.info("  ✓ 创建：生活随笔");
            return categoryRepository.save(Category.builder()
                .name("生活随笔")
                .description("生活记录和感悟")
                .build());
        });
    }

    private void createTags() {
        log.info("\n【标签初始化】");
        
        createTagIfNotExists("Java", "#FF6B6B");
        createTagIfNotExists("SpringBoot", "#4ECDC4");
        createTagIfNotExists("人工智能", "#45B7D1");
        createTagIfNotExists("读书笔记", "#FFA07A");
    }

    private void createTagIfNotExists(String name, String color) {
        tagRepository.findByName(name).orElseGet(() -> {
            log.info("  ✓ 创建：{} ({})", name, color);
            return tagRepository.save(Tag.builder()
                .name(name)
                .color(color)
                .build());
        });
    }

    private void displayAllData() {
        log.info("\n╔════════════════════════════════════════════╗");
        log.info("║           初始化完成 - 数据列表            ║");
        log.info("╠════════════════════════════════════════════╣");
        
        List<Category> categories = categoryRepository.findAll();
        log.info("║ 📁 分类 (共{}个):", categories.size());
        categories.forEach(c -> 
            log.info("║    • {} [ID:{}] ", c.getName(), c.getId())
        );
        
        List<Tag> tags = tagRepository.findAll();
        log.info("║ 🏷️  标签 (共{}个):", tags.size());
        tags.forEach(t -> 
            log.info("║    • {} {} ", t.getName(), t.getColor())
        );
        
        log.info("╚════════════════════════════════════════════╝");
        log.info("\n💡 提示：可以通过以下 API 查看数据:");
        log.info("   - 分类列表：GET http://localhost:8080/api/categories");
        log.info("   - 标签列表：GET http://localhost:8080/api/tags");
        log.info("   - 笔记列表：GET http://localhost:8080/api/notes");
    }
}
