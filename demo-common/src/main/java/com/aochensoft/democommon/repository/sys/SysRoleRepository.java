package com.aochensoft.democommon.repository.sys;

import com.aochensoft.democommon.entity.sys.SysRole;
import org.springframework.data.repository.CrudRepository;

/**
 * 系统角色持久层
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-01-20 12:15:15
 */
public interface SysRoleRepository extends CrudRepository<SysRole, Long> {
}
