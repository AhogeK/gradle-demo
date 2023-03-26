package com.aochensoft.demobackstage.controller.auth;

import com.aochensoft.democommon.dto.auth.SignUpRequest;
import com.aochensoft.democommon.service.sys.SysUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证相关接口
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-03-24 13:51:15
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final SysUserService userService;

    public AuthController(SysUserService userService) {
        this.userService = userService;
    }

    /**
     * 用户注册
     *
     * @param signUpRequest 注册信息
     * @return 注册结果
     */
    @PostMapping("/signup")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        userService.registerUser(signUpRequest);
        return ResponseEntity.ok().build();
    }
}
