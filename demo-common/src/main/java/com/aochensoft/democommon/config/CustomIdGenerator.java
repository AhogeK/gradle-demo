package com.aochensoft.democommon.config;

import cn.hutool.core.util.IdUtil;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 * JPA自定义ID生成器
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-01-20 13:04:07
 */
public class CustomIdGenerator implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return IdUtil.getSnowflakeNextId();
    }
}
