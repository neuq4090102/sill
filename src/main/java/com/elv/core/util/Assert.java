package com.elv.core.util;

import java.util.Collection;
import java.util.Map;

import com.elv.frame.constant.FrameworkError;
import com.elv.frame.exception.BusinessException;
import com.elv.frame.itf.IStatusCode;
import com.elv.frame.model.ServiceResult;
import com.elv.web.model.ValidationResult;

/**
 * @author lxh
 * @see org.springframework.util.Assert
 * @since 2020-07-27
 */
public final class Assert {

    public static void notNull(Object object, String msg) {
        isTrue(object == null, msg);
    }

    public static void notEmpty(Collection<?> collection, String msg) {
        isTrue(collection == null || collection.isEmpty(), msg);
    }

    public static void notEmpty(Map<?, ?> map, String msg) {
        isTrue(map == null || map.isEmpty(), msg);
    }

    public static void notBlank(String string, String msg) {
        isTrue(StrUtil.isBlank(string), msg);
    }

    public static void notBlank(String msg, String... strings) {
        for (String string : strings) {
            isTrue(StrUtil.isBlank(string), msg);
        }
    }

    public static void isTrue(boolean expression, String msg) {
        if (expression) {
            throw new RuntimeException(msg);
        }
    }

    public static void isTrue(boolean expression, IStatusCode statusCode) {
        if (expression) {
            throw new BusinessException(statusCode);
        }
    }

    public static void check(ServiceResult<?> serviceResult) {
        if (!serviceResult.isSuccess()) {
            throw new BusinessException(serviceResult.getCode(), serviceResult.getMsg());
        }
    }

    public static void check(ValidationResult validationResult) {
        if (validationResult.hasError()) {
            throw new BusinessException(FrameworkError.PARAM_ERROR, validationResult);
        }
    }

}
