package com.aochensoft.democommon.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Redis 前缀枚举
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-04-27 14:07:15
 */
@Getter
@RequiredArgsConstructor
public enum RedisPrefixEnum {

    JWT_TOKEN("jwt_token:"),
    ;

    private final String prefix;
}
