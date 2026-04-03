package com.iop.nexusmind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NexusMindApplication {

    public static void main(String[] args) {
        SpringApplication.run(NexusMindApplication.class, args);
    }
}
