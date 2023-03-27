package com.aochensoft.demobackstage.config;

import com.aochensoft.democommon.auth.CsrfTokenResponseHeaderBindingFilter;
import com.aochensoft.democommon.auth.CustomAuthenticationProvider;
import com.aochensoft.democommon.auth.JwtAuthenticationEntryPoint;
import com.aochensoft.democommon.auth.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;

import java.util.Collections;

/**
 * Spring Security 配置
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-02-13 19:36:42
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final CsrfTokenResponseHeaderBindingFilter csrfTokenResponseHeaderBindingFilter;

    private final CustomAuthenticationProvider customAuthenticationProvider;

    public WebSecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                             @Lazy JwtAuthenticationFilter jwtAuthenticationFilter,
                             @Lazy CsrfTokenResponseHeaderBindingFilter csrfTokenResponseHeaderBindingFilter,
                             @Lazy CustomAuthenticationProvider customAuthenticationProvider) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.csrfTokenResponseHeaderBindingFilter = csrfTokenResponseHeaderBindingFilter;
        this.customAuthenticationProvider = customAuthenticationProvider;
    }

    /**
     * 密码加密
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CookieCsrfTokenRepository csrfTokenRepository() {
        var repository = new CookieCsrfTokenRepository();
        repository.setCookiePath("/");              // 设置 CSRF Token 存储在 Cookie 中的路径
        repository.setCookieDomain("127.0.0.1");    // 设置 CSRF Token 存储在 Cookie 中的域名
        repository.setCookieMaxAge(3600);           // 设置 CSRF Token 存储在 Cookie 中的最大寿命（单位为秒）
        repository.setSecure(true);                 // 设置 CSRF Token 存储在 Cookie 中是否为安全 Cookie
        return repository;
    }

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
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(customAuthenticationProvider)
                // CSRF 认证
                .csrf()
                .csrfTokenRepository(csrfTokenRepository())
                .ignoringRequestMatchers("/api/auth/**")
                .and()
                .addFilterAfter(csrfTokenResponseHeaderBindingFilter, CsrfFilter.class)
        ;
        return http.build();
    }
}
