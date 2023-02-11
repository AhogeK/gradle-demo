package com.aochensoft.demobackstage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.aochensoft"})
@EntityScan(basePackages = {"com.aochensoft.democommon.entity"})
@EnableJpaRepositories(basePackages = {"com.aochensoft.democommon.dao"})
public class DemoBackstageApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoBackstageApplication.class, args);
    }

}
