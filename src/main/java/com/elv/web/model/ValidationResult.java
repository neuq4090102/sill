package com.elv.web.model;

import java.util.LinkedHashMap;

/**
 * @author lxh
 * @since 2020-03-20
 */
public class ValidationResult extends LinkedHashMap<String, Object> {

    private static final long serialVersionUID = -5943698108267618931L;

    public ValidationResult() {

    }

    public ValidationResult(String key, Object msg) {
        this.put(key, msg);
    }

    public void addError(String key, Object msg) {
        this.put(key, msg);
    }

    public boolean hasError() {
        return this.size() > 0;
    }
}
