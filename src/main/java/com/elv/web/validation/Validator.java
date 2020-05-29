package com.elv.web.validation;

import com.elv.core.util.Utils;
import com.elv.web.model.ValidationResult;
import com.elv.web.util.RequestUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author lxh
 * @date 2020-03-25
 */
public class Validator {

    private List<String> requires = new ArrayList<>();
    private List<String> notBlanks = new ArrayList<>();
    private List<String> numbers = new ArrayList<>();
    private List<IValidator> validators = new ArrayList<>();

    public List<String> getRequires() {
        return requires;
    }

    public List<String> getNotBlanks() {
        return notBlanks;
    }

    public List<String> getNumbers() {
        return numbers;
    }

    public List<IValidator> getValidators() {
        return validators;
    }

    public static Validator init() {
        return new Validator();
    }

    public Validator require(String... params) {
        this.getRequires().addAll(Arrays.asList(params));
        return this;
    }

    public Validator notBlank(String... params) {
        this.getNotBlanks().addAll(Arrays.asList(params));
        return this;
    }

    public Validator number(String... params) {
        this.getNumbers().addAll(Arrays.asList(params));
        return this;
    }

    public Validator range(String param, int min, int max) {
        this.getValidators().add(new RangeValidator(param, min, max));
        return this;
    }

    public Validator range(String param, long min, long max) {
        this.getValidators().add(new RangeValidator(param, min, max));
        return this;
    }

    public Validator range(String param, double min, double max) {
        this.getValidators().add(new RangeValidator(param, min, max));
        return this;
    }

    public Validator length(String param, int maxLength) {
        this.getValidators().add(new LengthValidator(param, maxLength));
        return this;
    }

    public Validator length(String param, int minLength, int maxLength) {
        this.getValidators().add(new LengthValidator(param, minLength, maxLength));
        return this;
    }

    public Validator enums(String param, Class<?> enumClass, String enumKey) {
        this.getValidators().add(new EnumValidator(param, enumClass, enumKey));
        return this;
    }

    public Validator regex(String param, String regex) {
        this.getValidators().add(new RegexValidator(param, regex));
        return this;
    }

    public ValidationResult validate() {
        ValidationResult result = new ValidationResult();

        for (String param : this.getRequires()) {
            if (StringUtils.isEmpty(RequestUtil.getStringParam(param))) {
                // 参数不足
                result.addError(param, "Required.");
            }
        }

        for (String param : this.getNotBlanks()) {
            if (StringUtils.isBlank(RequestUtil.getStringParam(param))) {
                // 不能为空
                result.addError(param, "Non-null.");
            }
        }

        for (String param : this.getNumbers()) {
            if (!Utils.isNum(RequestUtil.getStringParam(param))) {
                // 数值类型错误
                result.addError(param, "Non-number.");
            }
        }

        for (IValidator validator : this.getValidators()) {
            ValidationResult validationResult = validator.validate();
            if (validationResult != null) {
                result.putAll(validationResult);
            }
        }

        return result;
    }

}
