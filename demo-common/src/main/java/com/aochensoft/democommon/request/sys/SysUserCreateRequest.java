package com.aochensoft.democommon.request.sys;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 系统用户创建请求实体
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-02-27 22:48:42
 */
@Data
public class SysUserCreateRequest {

    /**
     * 用户名称
     */
    @NotNull(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotNull(message = "密码不能为空")
    private String password;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 电子邮箱
     */
    @NotNull(message = "邮箱不能为空")
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
