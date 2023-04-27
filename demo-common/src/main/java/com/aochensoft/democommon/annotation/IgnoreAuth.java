package com.aochensoft.democommon.annotation;

import java.lang.annotation.*;

/**
 * 忽略认证注解
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-04-27 16:04:28
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreAuth {
}
