package com.aochensoft.demobackstage.controller.auth;

import com.aochensoft.democommon.annotation.IgnoreAuth;
import com.aochensoft.democommon.dto.auth.SignInRequest;
import com.aochensoft.democommon.dto.auth.SignupDto;
import com.aochensoft.democommon.service.sys.SysUserService;
import com.aochensoft.democommon.vo.auth.AccessTokenVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@Validated
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
     * @param signupDto 注册信息
     * @return 注册结果
     */
    @IgnoreAuth
    @PostMapping("/signup")
    public ResponseEntity<AccessTokenVo> registerUser(@Valid @RequestBody SignupDto signupDto) {
        return ResponseEntity.ok().body(userService.registerUser(signupDto));
    }

    /**
     * 用户登录
     *
     * @param signInRequest 登录表单信息
     * @param request       请求
     * @param response      响应
     * @return 登录结果
     */
    @PostMapping("/signin")
    public ResponseEntity<Void> signIn(@Valid @RequestBody SignInRequest signInRequest, HttpServletRequest request,
                                       HttpServletResponse response) {
        userService.signIn(signInRequest, request, response);
        return ResponseEntity.ok().build();
    }
}
