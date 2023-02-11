package com.aochensoft.democommon.dao.sys;

import com.aochensoft.democommon.entity.sys.SysUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * 系统用户持久层
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-01-20 12:15:15
 */
@Repository
public interface SysUserDAO extends CrudRepository<SysUser, Long> {
}
