package com.aochensoft.democommon.service.sys.impl;

import com.aochensoft.democommon.entity.sys.SysUser;
import com.aochensoft.democommon.exception.AochenGlobalException;
import com.aochensoft.democommon.repository.sys.SysUserRepository;
import com.aochensoft.democommon.request.sys.SysUserCreateRequest;
import com.aochensoft.democommon.service.sys.SysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
}
