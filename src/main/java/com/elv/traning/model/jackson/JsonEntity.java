package com.elv.traning.model.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author lxh
 * @since 2020-04-17
 */

@JsonIgnoreProperties({ "extra", "uselessValue" })
public class JsonEntity implements Comparable {

    @JsonProperty("firstName")
    private String _first_name = "Bob";

    private int value = 666;
    @JsonIgnore
    private int internalValue = 888;

    private String extra = "fluffy";
    private int uselessValue = -13;

    public String get_first_name() {
        return _first_name;
    }

    public void set_first_name(String _first_name) {
        this._first_name = _first_name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getInternalValue() {
        return internalValue;
    }

    public void setInternalValue(int internalValue) {
        this.internalValue = internalValue;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public int getUselessValue() {
        return uselessValue;
    }

    public void setUselessValue(int uselessValue) {
        this.uselessValue = uselessValue;
    }

    @Override
    public int compareTo(Object o) {
        return this.getValue() - ((JsonEntity) o).getValue();
    }
}
