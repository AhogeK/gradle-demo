package com.aochensoft.demobackstage.config;

import com.aochensoft.democommon.auth.CustomAuthenticationProvider;
import com.aochensoft.democommon.auth.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

/**
 * Spring Security 配置
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-02-13 19:36:42
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationProvider customAuthenticationProvider;

    /**
     * 配置认证管理器
     *
     * @return AuthenticationManager
     */
    @Bean
    public ProviderManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(customAuthenticationProvider));
    }

    /**
     * 配置安全拦截机制
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                .anyRequest().permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(customAuthenticationProvider)
                .csrf().disable()
        ;
        return http.build();
    }
}
