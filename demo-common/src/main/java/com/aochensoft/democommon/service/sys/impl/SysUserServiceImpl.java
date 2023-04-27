package com.aochensoft.democommon.service.sys.impl;

import com.aochensoft.democommon.auth.JwtTokenProvider;
import com.aochensoft.democommon.dto.auth.SignInRequest;
import com.aochensoft.democommon.dto.auth.SignUpRequest;
import com.aochensoft.democommon.entity.sys.SysUser;
import com.aochensoft.democommon.exception.AochenGlobalException;
import com.aochensoft.democommon.repository.sys.SysUserRepository;
import com.aochensoft.democommon.service.sys.SysUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * 系统用户服务层实现
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-02-15 11:35:26
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    private final SysUserRepository sysUserRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final CsrfTokenRepository csrfTokenRepository;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public SysUserServiceImpl(SysUserRepository sysUserRepository, PasswordEncoder passwordEncoder,
                              JwtTokenProvider jwtTokenProvider, CsrfTokenRepository csrfTokenRepository,
                              AuthenticationManager authenticationManager) {
        this.sysUserRepository = sysUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.csrfTokenRepository = csrfTokenRepository;
        this.authenticationManager = authenticationManager;
    }

    private static boolean isValidEmail(String email) {
        // 邮箱格式的正则表达式
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        // 判断是否符合邮箱格式
        return Pattern.matches(regex, email);
    }

    @Override
    public SysUser loadUserById(Long userId) {
        return sysUserRepository.findById(userId)
                .orElseThrow(() -> new AochenGlobalException(HttpStatus.UNAUTHORIZED, "用户不存在"));
    }

    @Override
    public void registerUser(SignUpRequest signUpRequest) {
        // 判断邮箱是否正确
        if (!isValidEmail(signUpRequest.getEmail())) {
            throw new AochenGlobalException(HttpStatus.UNPROCESSABLE_ENTITY.value(), "邮箱格式不正确");
        }
        // 判断用户名和密码没有重复
        sysUserRepository.findByUsername(signUpRequest.getUsername()).ifPresent(user -> {
            throw new AochenGlobalException(HttpStatus.UNPROCESSABLE_ENTITY.value(), "用户名已存在");
        });
        sysUserRepository.findByEmail(signUpRequest.getEmail()).ifPresent(user -> {
            throw new AochenGlobalException(HttpStatus.UNPROCESSABLE_ENTITY.value(), "邮箱已存在");
        });
        SysUser insert = new SysUser();
        BeanUtils.copyProperties(signUpRequest, insert);
        // 加密密码
        insert.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        // 新增用户，报错抛出异常
        sysUserRepository.save(insert);
        if (insert.getId() == null) {
            throw new AochenGlobalException(HttpStatus.UNPROCESSABLE_ENTITY.value(), "注册失败");
        }
    }

    @Override
    public void signIn(SignInRequest signInRequest, HttpServletRequest request, HttpServletResponse response) {
        // 创建认证信息
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getAccount(), signInRequest.getPassword())
        );
        // 设置认证信息
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var jwt = jwtTokenProvider.generateToken(authentication);
        var jwtCookie = new Cookie("JWT", jwt);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(jwtTokenProvider.getJwtExpirationMs() / 1000);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);
        // 设置CSRF令牌
        var csrfToken = csrfTokenRepository.generateToken(request);
        var csrfCookie = new Cookie(csrfToken.getHeaderName(), csrfToken.getToken());
        csrfCookie.setHttpOnly(true);
        csrfCookie.setMaxAge(jwtTokenProvider.getJwtExpirationMs() / 1000);
        csrfCookie.setPath("/");
        csrfCookie.setSecure(true);
        response.addCookie(csrfCookie);
    }

    @Override
    public SysUser findUserByUsernameOrEmail(String username) {
        return sysUserRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new AochenGlobalException(HttpStatus.UNAUTHORIZED, "用户不存在"));
    }
}
