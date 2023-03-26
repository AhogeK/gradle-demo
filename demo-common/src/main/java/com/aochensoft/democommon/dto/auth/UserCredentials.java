package com.aochensoft.democommon.dto.auth;

import lombok.Data;

/**
 * 用户凭证
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-03-24 09:48:55
 */
@Data
public class UserCredentials {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
