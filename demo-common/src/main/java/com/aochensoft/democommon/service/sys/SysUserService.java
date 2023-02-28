package com.aochensoft.democommon.service.sys;

import com.aochensoft.democommon.request.sys.SysUserCreateRequest;

/**
 * 系统用户服务层
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-02-15 10:25:42
 */
public interface SysUserService {

    /**
     * 创建系统用户
     *
     * @param sysUserCreateRequest 系统用户创建请求实体
     * @return 用户ID
     */
    Long createSysUser(SysUserCreateRequest sysUserCreateRequest);
}
