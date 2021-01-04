package com.elv.core.tool.frame;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 上下文启动
 * <p>
 * VM参数：-Dspring.profiles.active=dev
 *
 * @author lxh
 * @since 2020-08-24
 */
public abstract class ContextLauncher {

    protected ApplicationContext context;

    protected void init() {
        try {
            context = new ClassPathXmlApplicationContext("classpath:META-INF/spring/applicationContext.xml");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    protected abstract void exec();

    protected void close() {
        if (context != null && context instanceof ClassPathXmlApplicationContext) {
            ClassPathXmlApplicationContext en = (ClassPathXmlApplicationContext) context;
            en.close();
        }
    }

    public ApplicationContext getContext() {
        return context;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    public final void deal() {
        init();
        exec();
        close();
    }
}
