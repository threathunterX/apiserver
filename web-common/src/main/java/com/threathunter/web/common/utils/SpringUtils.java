package com.threathunter.web.common.utils;

import org.springframework.context.ApplicationContext;

public class SpringUtils {

    public static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(Class<T> clazz, String name) {
        return applicationContext.getBean(name, clazz);
    }

    public static <T> T getBean(Class<T> clazz) {
        T bean = applicationContext.getBean(clazz);
        return bean;
    }


    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

}
