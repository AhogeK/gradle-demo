package com.aochensoft.democommon.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户注册请求
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-03-26 14:33:01
 */
@Data
public class SignUpRequest {

    /**
     * 用户名称
     */
    @Size(min = 6, max = 32, message = "用户名长度必须在6-32之间")
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 电子邮箱
     */
    @NotBlank(message = "电子邮箱不能为空")
    private String email;

    /**
     * 手机号码
     */
    private String mobileNum;

    /**
     * 性别
     */
    private Byte gender;
}
