package com.aochensoft.democommon.vo.auth;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 登录用户实体类
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-04-27 16:50:37
 */
@Data
public class LoginUser {

    private Long id;

    /**
     * 账号
     */
    private String account;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String mobileNum;

    /**
     * 性别
     */
    private Byte gender;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;
}
