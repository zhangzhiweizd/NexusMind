package com.iop.nexusmind.config;

import com.iop.nexusmind.entity.Category;
import com.iop.nexusmind.entity.Tag;
import com.iop.nexusmind.repository.CategoryRepository;
import com.iop.nexusmind.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    @Override
    public void run(String... args) {
        // 创建默认分类
        Category tech = categoryRepository.save(Category.builder()
            .name("技术学习")
            .description("编程和技术相关笔记")
            .build());

        Category life = categoryRepository.save(Category.builder()
            .name("生活随笔")
            .description("生活记录和感悟")
            .build());

        // 创建默认标签
        tagRepository.save(Tag.builder().name("Java").color("#FF6B6B").build());
        tagRepository.save(Tag.builder().name("SpringBoot").color("#4ECDC4").build());
        tagRepository.save(Tag.builder().name("人工智能").color("#45B7D1").build());
        tagRepository.save(Tag.builder().name("读书笔记").color("#FFA07A").build());

        System.out.println("=== 初始数据加载完成 ===");
    }
}
