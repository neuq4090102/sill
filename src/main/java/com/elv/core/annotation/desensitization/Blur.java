package com.elv.core.annotation.desensitization;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lxh
 * @since 2020-04-01
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Blur {

    String mask() default "*";

    int fromIdx() default 0;

    int toIdx() default 0;

    int setpSize() default 0;

    double ratio() default 0.0D;
}
