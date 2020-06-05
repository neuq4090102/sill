package com.elv.web.validation;

import java.util.regex.Pattern;

import com.elv.core.util.JsonUtil;
import com.elv.web.model.ValidationResult;
import com.elv.web.util.RequestUtil;

/**
 * @author lxh
 * @date 2020-04-21
 */
public class RegexValidator implements IValidator {
    private String param;
    private String regex;

    public RegexValidator() {
    }

    public RegexValidator(String param, String regex) {
        this.param = param;
        this.regex = regex;
    }

    public String getParam() {
        return param;
    }

    public String getRegex() {
        return regex;
    }

    @Override
    public ValidationResult validate() {
        String parameter = RequestUtil.getStringParam(this.getParam(), "");
        System.out.println(JsonUtil.toJson(this));
        if (!Pattern.compile(this.getRegex()).matcher(parameter).matches()) {
            //  格式不合法
            return new ValidationResult(this.getParam(), "Invalid format.");
        }
        return null;
    }
}
