package com.aochensoft.demobackstage;

import com.aochensoft.democommon.config.CustomReflectorProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableConfigurationProperties(CustomReflectorProperties.class)
@SpringBootApplication(scanBasePackages = {"com.aochensoft"})
@EntityScan(basePackages = {"com.aochensoft.democommon.entity"})
@EnableJpaRepositories(basePackages = {"com.aochensoft.democommon.repository"})
@PropertySource("classpath:application.yml")
public class DemoBackstageApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoBackstageApplication.class, args);
    }

}
