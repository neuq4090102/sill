package com.elv.web.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.elv.core.constant.FormEnum;
import com.elv.core.util.DateUtil;
import com.elv.core.constant.FormEnum.DateForm;
import com.elv.core.util.StrUtil;
import com.elv.web.model.ValidationResult;
import com.elv.web.util.RequestUtil;

/**
 * @author lxh
 * @since 2020-03-25
 */
public class Validator {

    private List<String> requires = new ArrayList<>();
    private List<String> notBlanks = new ArrayList<>();
    private List<String> numbers = new ArrayList<>();
    private List<IValidator> validators = new ArrayList<>();
    private Map<DateForm, List<String>> dateFormMap = new HashMap<>();

    public static Validator of() {
        return new Validator();
    }

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

    public Map<DateForm, List<String>> getDateFormMap() {
        return dateFormMap;
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

    public Validator dateForm(String... params) {
        List<String> dateForms = Optional.ofNullable(this.getDateFormMap().get(DateForm.DATE))
                .orElse(new ArrayList<>());
        dateForms.addAll(Arrays.asList(params));
        dateFormMap.put(DateForm.DATE, dateForms);
        return this;
    }

    public Validator timeForm(String... params) {
        List<String> timeForms = Optional.ofNullable(this.getDateFormMap().get(DateForm.TIME))
                .orElse(new ArrayList<>());
        timeForms.addAll(Arrays.asList(params));
        dateFormMap.put(DateForm.TIME, timeForms);
        return this;
    }

    public Validator dateTimeForm(String... params) {
        List<String> dateTimeForms = Optional.ofNullable(this.getDateFormMap().get(DateForm.DATE_TIME))
                .orElse(new ArrayList<>());
        dateTimeForms.addAll(Arrays.asList(params));
        dateFormMap.put(DateForm.DATE_TIME, dateTimeForms);
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

        // 参数不足校验
        for (String param : this.getRequires()) {
            if (StrUtil.isEmpty(RequestUtil.getStrParam(param))) {
                result.addError(param, "Required.");
            }
        }

        // 不能为空校验
        for (String param : this.getNotBlanks()) {
            if (StrUtil.isBlank(RequestUtil.getStrParam(param))) {
                result.addError(param, "Non-null.");
            }
        }

        // 数值类型校验
        for (String param : this.getNumbers()) {
            if (!StrUtil.isDigit(RequestUtil.getStrParam(param))) {
                result.addError(param, "Non-number.");
            }
        }

        // 日期格式校验
        dateFormMap.forEach((key, value) -> {
            value.stream().forEach(param -> {
                String paramVal = RequestUtil.getStrParam(param);
                if (key == DateForm.DATE && !DateUtil.isDate(paramVal)) {
                    result.addError(param, "Invalid date form, please refer:" + key.getPattern());
                } else if (key == DateForm.TIME && !DateUtil.isTime(paramVal)) {
                    result.addError(param, "Invalid time form, please refer:" + key.getPattern());
                } else if (key == DateForm.DATE_TIME && !DateUtil.isDateTime(paramVal)) {
                    result.addError(param, "Invalid dateTime form, please refer:" + key.getPattern());
                }
            });
        });

        // 其他校验：范围、长度、枚举、正则等
        for (IValidator validator : this.getValidators()) {
            ValidationResult validationResult = validator.validate();
            if (validationResult != null) {
                result.putAll(validationResult);
            }
        }

        return result;
    }

}
