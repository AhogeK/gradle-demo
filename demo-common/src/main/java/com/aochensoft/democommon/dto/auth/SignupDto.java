package com.aochensoft.democommon.dto.auth;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 用户注册请求
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-03-26 14:33:01
 */
@Data
public class SignupDto {

    /**
     * 用户名称
     */
    @Length(min = 3, max = 64, message = "用户名长度必须在3-64之间")
    @Pattern(regexp = "^[a-zA-Z0-9_.+\\-@]{3,64}$", message = "账号格式错误")
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 昵称
     */
    @Length(min = 1, max = 64, message = "用户昵称长度必须在1-64之间")
    @NotBlank(message = "用户昵称不能为空")
    private String nickname;

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
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "邮箱格式不正确")
    @NotBlank(message = "电子邮箱不能为空")
    private String email;

    /**
     * 手机号码
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
    private String mobileNum;

    /**
     * 性别 0-女 1-男
     */
    @Min(value = 0, message = "性别格式错误")
    @Max(value = 1, message = "性别格式错误")
    private Byte gender;
}
