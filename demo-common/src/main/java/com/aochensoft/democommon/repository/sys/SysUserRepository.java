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
}
