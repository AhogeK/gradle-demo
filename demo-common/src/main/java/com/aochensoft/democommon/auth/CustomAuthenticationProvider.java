package com.aochensoft.democommon.auth;

import com.aochensoft.democommon.entity.sys.SysUser;
import com.aochensoft.democommon.exception.AochenGlobalException;
import com.aochensoft.democommon.service.sys.SysUserService;
import com.aochensoft.democommon.service.sys.impl.SysUserServiceImpl;
import com.aochensoft.democommon.util.BeanUtil;
import com.aochensoft.democommon.vo.auth.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自定义认证提供者
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-03-27 19:29:20
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SysUserService sysUserService = BeanUtil.getBean(SysUserServiceImpl.class);
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        SysUser user = sysUserService.findUserByUsername(username);
        if (user == null) {
            throw new AochenGlobalException(HttpStatus.UNAUTHORIZED, "用户不存在");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AochenGlobalException(HttpStatus.UNAUTHORIZED, "密码错误");
        }
        // 更新用户的最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        sysUserService.updateById(user);
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(user, loginUser);
        return new UsernamePasswordAuthenticationToken(loginUser, null, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
