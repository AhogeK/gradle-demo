package com.aochensoft.democommon.auth;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import com.aochensoft.democommon.annotation.IgnoreAuth;
import com.aochensoft.democommon.exception.AochenGlobalException;
import com.aochensoft.democommon.exception.GlobalExceptionHandler;
import com.aochensoft.democommon.vo.auth.LoginUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * JWT 认证过滤器
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-03-24 10:30:02
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private final JwtTokenProvider jwtTokenProvider;
    private final GlobalExceptionHandler globalExceptionHandler;

    /**
     * 忽略认证的路径
     */
    @Value("${app.ignoredPatterns}")
    private List<String> ignoredPatterns;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            if (requiresAuthentication(request)) {
                String jwt = getJwtFromRequest(request);
                if (CharSequenceUtil.hasLetter(jwt) && jwtTokenProvider.validateToken(jwt)) {
                    // 获取登录用户
                    LoginUser loginUser = jwtTokenProvider.getLoginUserFromJWT(jwt);
                    Collection<? extends GrantedAuthority> authorities = jwtTokenProvider.getGrantedAuthorities(jwt);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(loginUser, null, authorities);
                    authentication.setDetails(loginUser);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    throw new AochenGlobalException(HttpStatus.UNAUTHORIZED, "认证失败");
                }
            }
        } catch (AochenGlobalException e) {
            response.setStatus(e.getStatus());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write(JSONUtil.toJsonStr(globalExceptionHandler
                    .buildErrorResponseBody(e, e.getMessage(), HttpStatusCode.valueOf(e.getStatus())
                            , new ServletWebRequest(request))));
            return;
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write(JSONUtil.toJsonStr(globalExceptionHandler
                    .buildErrorResponseBody(e, e.getMessage(),
                            HttpStatusCode.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                            new ServletWebRequest(request))));
            return;
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 判断请求是否需要认证
     *
     * @param request 请求
     * @return 是否需要认证
     */
    private boolean requiresAuthentication(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        for (String pattern : ignoredPatterns) {
            if (Pattern.matches(pattern, requestURI)) {
                return false;
            }
        }
        // 获取HandlerMethod
        HandlerMethod handlerMethod;
        try {
            handlerMethod = getHandlerMethod(request);
        } catch (HttpRequestMethodNotSupportedException e) {
            throw new AochenGlobalException(HttpStatus.METHOD_NOT_ALLOWED, "请求方式不支持");
        } catch (Exception e) {
            throw new AochenGlobalException(HttpStatus.UNAUTHORIZED, "认证失败");
        }
        // 不需要认证
        return handlerMethod == null || !handlerMethod.hasMethodAnnotation(IgnoreAuth.class);
    }

    /**
     * 从请求中获取 JWT TOKEN
     *
     * @param request 用户请求
     * @return JWT TOKEN
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * 获取HandlerMethod
     *
     * @param request 用户请求
     * @return 被请求的方法
     * @throws Exception 异常
     */
    private HandlerMethod getHandlerMethod(HttpServletRequest request) throws Exception {
        HandlerExecutionChain handlerExecutionChain = requestMappingHandlerMapping.getHandler(request);
        if (handlerExecutionChain != null) {
            return (HandlerMethod) handlerExecutionChain.getHandler();
        }
        return null;
    }
}
