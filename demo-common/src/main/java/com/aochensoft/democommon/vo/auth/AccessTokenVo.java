package com.aochensoft.democommon.vo.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 认证token视图
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-04-27 17:35:45
 */
@Data
@AllArgsConstructor
public class AccessTokenVo {

    /**
     * token
     */
    private String token;
}
