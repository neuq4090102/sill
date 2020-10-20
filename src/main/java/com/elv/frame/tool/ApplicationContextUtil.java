package com.elv.frame.tool;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author lxh
 * @since 2020-08-27
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtil.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(clazz);
    }

    public static Object getBean(String beanId) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(beanId);
    }

    public static <T> T getBean(String beanId, Class<T> clazz) {
        if (beanId == null || applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(beanId, clazz);
    }

}
