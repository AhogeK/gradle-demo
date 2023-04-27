package com.aochensoft.democommon.service.sys;

import com.aochensoft.democommon.dto.auth.SignInRequest;
import com.aochensoft.democommon.dto.auth.SignupDto;
import com.aochensoft.democommon.entity.sys.SysUser;
import com.aochensoft.democommon.vo.auth.AccessTokenVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 系统用户服务层
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-02-15 10:25:42
 */
public interface SysUserService {

    /**
     * 根据用户ID加载用户
     *
     * @param userId 用户ID
     * @return 用户实体
     */
    SysUser loadUserById(Long userId);

    /**
     * 注册用户
     *
     * @param signupDto 注册信息
     * @return access token
     */
    AccessTokenVo registerUser(SignupDto signupDto);

    /**
     * 登录
     *
     * @param signInRequest 登录请求实体
     */
    void signIn(SignInRequest signInRequest, HttpServletRequest request, HttpServletResponse response);

    /**
     * 通过用户名或密码查询用户
     *
     * @param username 用户名
     * @return 用户实体
     */
    SysUser findUserByUsernameOrEmail(String username);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户实体
     */
    SysUser findUserByUsername(String username);

    /**
     * 根据id更新用户实体
     *
     * @param user 用户实体
     */
    void updateById(SysUser user);
}
