package com.aochensoft.democommon.repository.sys;

import com.aochensoft.democommon.entity.sys.SysUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * 系统用户持久层
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-01-20 12:15:15
 */
public interface SysUserRepository extends CrudRepository<SysUser, Long> {

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户
     */
    Optional<SysUser> findByEmail(String email);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户
     */
    Optional<SysUser> findByUsername(String username);

    /**
     * 根据用户名或邮箱查询用户
     *
     * @param username 用户名
     * @param email    邮箱
     * @return 用户
     */
    Optional<SysUser> findByUsernameOrEmail(String username, String email);

    /**
     * 根据昵称查询用户数量
     *
     * @param nickname 昵称
     * @return 用户
     */
    Optional<Integer> countByNickname(String nickname);

    /**
     * 根据邮箱查询用户数量
     *
     * @param email 邮箱
     * @return 用户
     */
    Optional<Integer> countByEmail(String email);

    /**
     * 根据用户名查询用户数量
     *
     * @param username 用户名
     * @return 用户
     */
    Optional<Integer> countByUsername(String username);

    /**
     * 根据手机号码查询用户数量
     *
     * @param mobileNum 手机号码
     * @return 用户
     */
    Optional<Integer> countByMobileNum(String mobileNum);
}
