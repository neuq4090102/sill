package com.elv.core.tool.application.log.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 业务日志注解（BLog=BusinessLog）
 *
 * @author lxh
 * @since 2021-08-09
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BLog {

    /**
     * 描述
     *
     * @return java.lang.String
     */
    String desc();

    /**
     * 映射要展示的属性
     * <p>
     * 当前属性值如果未变化，即使mappingField对应的属性值发生变化也不展示
     *
     * @return java.lang.String
     */
    String mappingField() default "";

    /**
     * 前缀
     *
     * @return java.lang.String
     */
    String prefix() default "";

    /**
     * 后缀
     *
     * @return java.lang.String
     */
    String suffix() default "";

    /**
     * 枚举类
     * <p>
     * 建议：枚举类至少有【主属性】和【描述】俩属性
     *
     * @return java.lang.Class
     */
    Class<? extends Enum> enumClass() default Enum.class;

    /**
     * 枚举类的key
     * <p>
     * 若指定枚举的主属性，则用之；若未指定，则自动识别
     *
     * @return java.lang.String
     * @see #enumClass() enumClass指定后此配置方可生效
     */
    String enumKey() default "";

    /**
     * 枚举类的描述
     *
     * @return java.lang.String
     */
    String enumDesc() default "";

    /**
     * 是否复合枚举
     * <p>
     * eg：floatingWeek（浮动星期）的值为"1、2"，表示"星期日、星期一"
     *
     * @return boolean
     */
    boolean complexEnum() default false;

    /**
     * 复合枚举分隔符
     *
     * @return java.lang.String
     * @see #complexEnum() complexEnum为true时此配置方可生效
     */
    String enumDelimiter() default "、";

    /**
     * 分组编码
     *
     * @return java.lang.String
     */
    String groupCode() default "";

    /**
     * 分组描述
     *
     * @return java.lang.String
     */
    String groupDesc() default "";

    /**
     * 组内分隔符
     *
     * @return java.lang.String
     */
    String groupDelimiter() default "";

    /**
     * 分组排序（从小到大）
     *
     * @return int
     */
    int groupSort() default 0;

    /**
     * 值未修改是否展示
     * <p>
     * 注意：依赖已改变的属性
     *
     * @return boolean
     */
    boolean uptKeep() default false;

}
