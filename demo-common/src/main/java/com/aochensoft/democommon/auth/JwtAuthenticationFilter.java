package com.aochensoft.democommon.auth;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.aochensoft.democommon.entity.sys.SysUser;
import com.aochensoft.democommon.exception.ErrorResponse;
import com.aochensoft.democommon.service.sys.SysUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

/**
 * JWT 认证过滤器
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-03-24 10:30:02
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private final SysUserService userService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, SysUserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (requiresAuthentication(request)) {
            String jwt = getJwtFromRequest(request);
            if (CharSequenceUtil.hasLetter(jwt) && jwtTokenProvider.validateToken(jwt)) {
                // 获取登录用户
                SysUser loginUser = jwtTokenProvider.getLoginUserFromJWT(jwt);
                Long userId = jwtTokenProvider.getUserIdFromJWT(jwt);
                SysUser user = userService.loadUserById(userId);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authentication.setDetails(user);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");
                // LocalDateTime 转 yyyy-MM-dd HH:mm:ss
                JSONConfig jsonConfig = JSONConfig.create().setDateFormat("yyyy-MM-dd HH:mm:ss");
                response.getWriter().write(JSONUtil.toJsonStr(new ErrorResponse(HttpServletResponse.SC_UNAUTHORIZED,
                        "认证失败"), jsonConfig));
                return;
            }
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
        AntPathMatcher matcher = new AntPathMatcher();
        List<String> permitAllPatterns = List.of("/api/auth/**");
        for (String pattern : permitAllPatterns) {
            if (matcher.match(pattern, request.getServletPath())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 从请求中获取 JWT
     *
     * @param request 请求
     * @return JWT
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            List<String> collect = Stream.of(cookies)
                    .filter(cookie -> cookie.getName().equals("token"))
                    .map(Cookie::getValue)
                    .toList();
            if (!collect.isEmpty()) {
                return collect.get(0);
            }
        }
        return null;
    }
}
