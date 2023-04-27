package com.aochensoft.democommon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author AhogeK ahogek@gmail.com
 * @since 2023-04-27 19:40:38
 */
@Configuration
public class CustomBeanConfig {

    /**
     * 密码加密
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
