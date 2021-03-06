package com.elv.web.validation;

import com.elv.core.util.StrUtil;
import com.elv.web.model.ValidationResult;
import com.elv.web.util.RequestUtil;

/**
 * 区间验证
 *
 * @author lxh
 * @since 2020-03-25
 */
public class RangeValidator<T> implements IValidator {

    private String param;
    private T min;
    private T max;

    public RangeValidator() {
    }

    public RangeValidator(String param, T min, T max) {
        this.param = param;
        this.min = min;
        this.max = max;
    }

    public String getParam() {
        return param;
    }

    public T getMin() {
        return min;
    }

    public T getMax() {
        return max;
    }

    @Override
    public ValidationResult validate() {
        String parameter = RequestUtil.getStrParam(this.getParam(), "");
        if (StrUtil.isDigit(parameter)) {
            if (min instanceof Integer) {
                int intVal = Integer.parseInt(parameter);
                if (intVal >= (Integer) getMin() && intVal <= (Integer) getMax()) {
                    return null;
                }
            } else if (min instanceof Long) {
                long longVal = Long.parseLong(parameter);
                if (longVal >= (Long) getMin() && longVal <= (Long) getMax()) {
                    return null;
                }
            } else if (min instanceof Double) {
                double doubleVal = Double.parseDouble(parameter);
                if (doubleVal >= (Double) getMin() && doubleVal <= (Double) getMax()) {
                    return null;
                }
            }
        }

        // "必须在" + min + "和" + max + "之间"
        return new ValidationResult(this.getParam(), "The value should be between " + min + " and " + max + ".");
    }
}
