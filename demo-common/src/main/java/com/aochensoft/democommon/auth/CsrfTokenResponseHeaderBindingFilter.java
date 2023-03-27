package com.aochensoft.democommon.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * CSRF 过滤器
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-03-27 20:11:13
 */
@Component
public class CsrfTokenResponseHeaderBindingFilter extends OncePerRequestFilter {

    private final CsrfTokenRepository csrfTokenRepository;

    public CsrfTokenResponseHeaderBindingFilter(CsrfTokenRepository csrfTokenRepository) {
        this.csrfTokenRepository = csrfTokenRepository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        CsrfToken csrfToken = csrfTokenRepository.loadToken(request);
        if (csrfToken != null) {
            response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
        }
        filterChain.doFilter(request, response);
    }
}
