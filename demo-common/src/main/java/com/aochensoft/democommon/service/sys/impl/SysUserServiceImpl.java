package com.aochensoft.democommon.service.sys.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.crypto.symmetric.AES;
import com.aochensoft.democommon.dto.auth.SignUpRequest;
import com.aochensoft.democommon.entity.sys.SysUser;
import com.aochensoft.democommon.exception.AochenGlobalException;
import com.aochensoft.democommon.repository.sys.SysUserRepository;
import com.aochensoft.democommon.request.sys.SysUserCreateRequest;
import com.aochensoft.democommon.service.sys.SysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    public SysUserServiceImpl(SysUserRepository sysUserRepository) {
        this.sysUserRepository = sysUserRepository;
    }

    private static boolean isValidEmail(String email) {
        // 邮箱格式的正则表达式
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        // 判断是否符合邮箱格式
        return Pattern.matches(regex, email);
    }

    @Override
    public Long createSysUser(SysUserCreateRequest sysUserCreateRequest) {
        SysUser sysUser = new SysUser();
        sysUserRepository.findByUsername(sysUserCreateRequest.getUsername()).ifPresent(user -> {
            throw new AochenGlobalException(HttpStatus.UNPROCESSABLE_ENTITY.value(), "用户名已存在");
        });
        sysUserRepository.findByEmail(sysUserCreateRequest.getEmail()).ifPresent(user -> {
            throw new AochenGlobalException(HttpStatus.UNPROCESSABLE_ENTITY.value(), "邮箱已存在");
        });
        BeanUtils.copyProperties(sysUserCreateRequest, sysUser);
        sysUserRepository.save(sysUser);
        return sysUser.getId();
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
        // 解密密码，如果密码少于6位，抛出异常
        Digester digester = new Digester(DigestAlgorithm.SHA256);
        byte[] sha256Byte = digester.digest("ahogek:gradle-demo");
        AES aes = SecureUtil.aes(sha256Byte);
        String password = aes.decryptStr(signUpRequest.getPassword());
        if (password.length() < 6) {
            throw new AochenGlobalException(HttpStatus.UNPROCESSABLE_ENTITY.value(), "密码长度不能少于6位");
        } else if (password.length() > 128) {
            throw new AochenGlobalException(HttpStatus.UNPROCESSABLE_ENTITY.value(), "密码长度不能大于128位");
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
        byte[] digest = digester.digest(signUpRequest.getPassword());
        String storagePassword = aes.encryptHex(digest);
        signUpRequest.setPassword(storagePassword);
        // 新增用户，报错抛出异常
        sysUserRepository.save(insert);
        if (insert.getId() == null) {
            throw new AochenGlobalException(HttpStatus.UNPROCESSABLE_ENTITY.value(), "注册失败");
        }
    }
}
