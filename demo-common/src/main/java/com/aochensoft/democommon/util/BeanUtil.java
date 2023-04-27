package com.aochensoft.democommon.util;


import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Bean工具类
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-04-18 16:44:17
 */
@Component
public class BeanUtil implements ApplicationContextAware, BeanFactoryAware {
    private static ApplicationContext applicationContext;
    private static BeanFactory beanFactory;

    /**
     * 获取bean
     *
     * @param clazz bean类型
     * @param <T>   bean类型
     * @return bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    /**
     * 获取bean
     *
     * @param name bean名称
     * @return bean
     */
    public static Object getBean(String name) {
        return beanFactory.getBean(name);
    }

    /**
     * 赋值静态变量
     *
     * @param applicationContext spring上下文
     */
    private static synchronized void setStaticApplicationContext(ApplicationContext applicationContext) {
        BeanUtil.applicationContext = applicationContext;
    }

    /**
     * 赋值静态变量
     *
     * @param beanFactory spring bean工厂
     */
    private static synchronized void setStaticBeanFactory(BeanFactory beanFactory) {
        BeanUtil.beanFactory = beanFactory;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        setStaticApplicationContext(applicationContext);
    }

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        setStaticBeanFactory(beanFactory);
    }
}
