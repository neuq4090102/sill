package com.elv.web.validation;

import com.elv.core.util.BeanUtil;
import com.elv.web.model.ValidationResult;
import com.elv.web.util.RequestUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 枚举验证
 * 1.建议优先使用区间验证（RangeValidator）
 * 2.枚举值是非线性的，且需要强校验，可以使用此验证
 *
 * @author lxh
 * @date 2020-03-26
 */
public class EnumValidator implements IValidator {

    private String param;
    private Class<?> enumClass;
    private String enumKey;

    public EnumValidator() {
    }

    public EnumValidator(String param, Class<?> enumClass, String enumKey) {
        this.param = param;
        this.enumClass = enumClass;
        this.enumKey = enumKey;
    }

    public String getParam() {
        return param;
    }

    public Class<?> getEnumClass() {
        return enumClass;
    }

    public String getEnumKey() {
        return enumKey;
    }

    @Override
    public ValidationResult validate() {
        String parameter = RequestUtil.getStringParam(this.getParam(), "");
        if (!this.getEnumClass().isEnum()) {
            // 非枚举类
            return new ValidationResult(this.getParam(), this.getEnumClass().getSimpleName() + " isn't a enum class.");
        }
        Class<Enum> enumClass = (Class<Enum>) this.getEnumClass();

        Optional<Field> targetField = BeanUtil.getFields(enumClass, false).stream().peek(field -> {
        }).filter(field -> !field.isEnumConstant() && field.getName().equals(this.getEnumKey())).findFirst();
        if (!targetField.isPresent()) {
            // 未查到枚举类有对应的属性名称
            return new ValidationResult(this.getParam(),
                    this.getEnumClass().getSimpleName() + " hasn't the field named by " + this.getEnumKey() + ".");
        }

        try {
            Method method = BeanUtil.getGetterMap(enumClass).get(this.getEnumKey());
            if (method == null) {
                return new ValidationResult(this.getParam(),
                        this.getEnumClass().getSimpleName() + " not found getter of " + this.getEnumKey() + ".");
            }

            List<String> enumKeys = new ArrayList<>();
            for (Enum item : enumClass.getEnumConstants()) {
                enumKeys.add(method.invoke(item).toString());
            }

            if (!enumKeys.contains(parameter)) {
                // 枚举参数不合法
                return new ValidationResult(this.getParam(), "Invalid enum param.");
            }
        } catch (Exception e) {
            // 解析key值异常
            return new ValidationResult(this.getParam(), enumClass.getSimpleName() + " parse error:" + e);
        }

        return null;
    }
}
