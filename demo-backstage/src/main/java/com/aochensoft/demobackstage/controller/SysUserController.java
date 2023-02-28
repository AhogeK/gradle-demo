package com.aochensoft.demobackstage.controller;

import com.aochensoft.democommon.request.sys.SysUserCreateRequest;
import com.aochensoft.democommon.service.sys.SysUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 系统用户控制层
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-02-14 13:44:20
 */
@RestController
@RequestMapping("/api/sys-user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService sysUserService;

    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Hello World");
    }

    /**
     * 创建系统用户
     *
     * @param sysUserCreateRequest 系统用户创建请求实体
     * @return 接口响应实体
     */
    @PostMapping
    public ResponseEntity<Long> createSysUser(@Valid @RequestBody SysUserCreateRequest sysUserCreateRequest) {
        return ResponseEntity.ok(sysUserService.createSysUser(sysUserCreateRequest));
    }
}
