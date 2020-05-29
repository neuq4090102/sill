package com.elv.web.validation;

import com.elv.sill.api.model.ValidationResult;
import com.elv.sill.api.util.RequestUtil;
import com.elv.web.model.ValidationResult;

/**
 * 长度验证
 *
 * @author lxh
 * @date 2020-03-25
 */
public class LengthValidator implements IValidator {

    private String param;
    private int minLength;
    private int maxLength;

    public LengthValidator() {
    }

    public LengthValidator(String param, int maxLength) {
        this.param = param;
        this.maxLength = maxLength;
    }

    public LengthValidator(String param, int minLength, int maxLength) {
        this.param = param;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    public String getParam() {
        return param;
    }

    public int getMinLength() {
        return minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    @Override
    public ValidationResult validate() {
        String parameter = RequestUtil.getStringParam(this.getParam(), "");
        int length = parameter.length();
        if (length > this.getMaxLength()) {
            // 长度不能超过
            return new ValidationResult(this.getParam(), String.format(" The max length is %d.", this.getMaxLength()));
        } else if (length < this.getMinLength()) {
            // 长度不能低于
            return new ValidationResult(this.getParam(), String.format(" The min length %d.", this.getMinLength()));
        }
        return null;
    }
}
