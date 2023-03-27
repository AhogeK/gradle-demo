package com.aochensoft.democommon.dto.auth;

import lombok.Data;

/**
 * 登录请求
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-03-27 19:12:10
 */
@Data
public class SignInRequest {

    /**
     * 账号 (用户名或密码)
     */
    private String account;

    /**
     * 密码
     */
    private String password;
}
