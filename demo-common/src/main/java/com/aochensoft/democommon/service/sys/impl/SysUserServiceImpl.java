package com.aochensoft.democommon.service.sys.impl;

import com.aochensoft.democommon.auth.JwtTokenProvider;
import com.aochensoft.democommon.dto.auth.SignInRequest;
import com.aochensoft.democommon.dto.auth.SignupDto;
import com.aochensoft.democommon.entity.sys.SysUser;
import com.aochensoft.democommon.exception.AochenGlobalException;
import com.aochensoft.democommon.repository.sys.SysUserRepository;
import com.aochensoft.democommon.service.sys.SysUserService;
import com.aochensoft.democommon.vo.auth.AccessTokenVo;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 系统用户服务层实现
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-02-15 11:35:26
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    public static final String USER_NOT_EXIST = "用户不存在";

    private final SysUserRepository sysUserRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public SysUser loadUserById(Long userId) {
        return sysUserRepository.findById(userId)
                .orElseThrow(() -> new AochenGlobalException(HttpStatus.UNAUTHORIZED, USER_NOT_EXIST));
    }

    @Override
    @Transactional
    public AccessTokenVo registerUser(SignupDto signupDto) {
        // 将 account 所有大写字母小写
        if (Objects.nonNull(signupDto.getUsername())) {
            signupDto.setUsername(signupDto.getUsername().toLowerCase());
        }

        // 判断用户名/邮箱/昵称有没有重复
        if (sysUserRepository.countByUsername(signupDto.getUsername()).orElse(0) > 0) {
            throw new AochenGlobalException(HttpStatus.UNPROCESSABLE_ENTITY, "用户名已存在");
        }
        if (sysUserRepository.countByEmail(signupDto.getEmail()).orElse(0) > 0) {
            throw new AochenGlobalException(HttpStatus.UNPROCESSABLE_ENTITY, "邮箱已存在");
        }
        if (sysUserRepository.countByNickname(signupDto.getNickname()).orElse(0) > 0) {
            throw new AochenGlobalException(HttpStatus.UNPROCESSABLE_ENTITY, "昵称已存在");
        }
        if (StringUtils.isNotBlank(signupDto.getMobileNum()) &&
                sysUserRepository.countByMobileNum(signupDto.getMobileNum()).orElse(0) > 0) {
            throw new AochenGlobalException(HttpStatus.UNPROCESSABLE_ENTITY, "手机号已存在");
        }

        // 判断密码是否为sha256加密
        if (!signupDto.getPassword().matches("^[a-fA-F0-9]{64}$")) {
            throw new AochenGlobalException(HttpStatus.UNPROCESSABLE_ENTITY, "密码未加密");
        }

        SysUser insert = new SysUser();
        BeanUtils.copyProperties(signupDto, insert);

        // 加密密码
        insert.setPassword(passwordEncoder.encode(signupDto.getPassword()));

        // 新增用户，报错抛出异常
        sysUserRepository.save(insert);

        SysUser user = sysUserRepository.findById(insert.getId()).orElseThrow(() ->
                new AochenGlobalException(HttpStatus.UNAUTHORIZED, "注册失败"));

        // 注册成功的话直接算登入成功
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), signupDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtTokenProvider.generateToken(authenticate);
        return new AccessTokenVo(token);
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
    }

    @Override
    public SysUser findUserByUsernameOrEmail(String username) {
        return sysUserRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new AochenGlobalException(HttpStatus.UNAUTHORIZED, USER_NOT_EXIST));
    }

    @Override
    public SysUser findUserByUsername(String username) {
        return sysUserRepository.findByUsername(username)
                .orElseThrow(() -> new AochenGlobalException(HttpStatus.UNAUTHORIZED, USER_NOT_EXIST));
    }

    @Override
    public void updateById(SysUser user) {
        sysUserRepository.save(user);
    }
}
