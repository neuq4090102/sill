package com.elv.core.tool.application.log.vo;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 对比明细对象
 *
 * <p>
 * 对比说明解读：
 * <p>
 * *  @BLog(desc = "类型", enumClass = TypeEnum.class, prefix = "【", suffix = "】")
 * *  private int type;
 * <p>
 * {
 * "fieldName": "type",
 * "fieldDesc": "类型",
 * "prefix": "【",
 * "suffix": "】",
 * "oldValue": 0,
 * "oldValueDesc": "",
 * "newValue": 2,
 * "newValueDesc": "软包-有保底",
 * "sort": 0,
 * "beforeDesc": "【】",
 * "afterDesc": "【软包-有保底】",
 * "after": "【2】",
 * "before": "【0】"
 * }
 * </p>
 *
 * @author lxh
 * @since 2021-08-09
 */
@JsonInclude(Include.NON_NULL)
public class BLogDetailVO {

    /**
     * 属性
     */
    private String fieldName;
    /**
     * 属性描述
     */
    private String fieldDesc;
    /**
     * 旧值
     */
    private Object oldValue;
    /**
     * 旧值描述
     */
    private Object oldValueDesc;
    /**
     * 新值
     */
    private Object newValue;
    /**
     * 新值描述
     */
    private Object newValueDesc;
    /**
     * 前缀
     * <p>
     * 针对业务前缀，比如【价格的币种】等，可根据业务自行赋值
     */
    private String prefix = "";
    /**
     * 后缀
     */
    private String suffix = "";
    /**
     * 值未修改是否展示
     */
    private boolean uptKeep;
    /**
     * 分组排序
     */
    private int sort;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public void setOldValue(Object oldValue) {
        this.oldValue = oldValue;
    }

    public Object getOldValueDesc() {
        return oldValueDesc;
    }

    public void setOldValueDesc(Object oldValueDesc) {
        this.oldValueDesc = oldValueDesc;
    }

    public Object getNewValue() {
        return newValue;
    }

    public void setNewValue(Object newValue) {
        this.newValue = newValue;
    }

    public Object getNewValueDesc() {
        return newValueDesc;
    }

    public void setNewValueDesc(Object newValueDesc) {
        this.newValueDesc = newValueDesc;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public boolean isUptKeep() {
        return uptKeep;
    }

    public void setUptKeep(boolean uptKeep) {
        this.uptKeep = uptKeep;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public static BLogDetailVO of() {
        return new BLogDetailVO();
    }

    public BLogDetailVO fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public BLogDetailVO fieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
        return this;
    }

    public BLogDetailVO oldValue(Object oldValue) {
        this.oldValue = oldValue;
        return this;
    }

    public BLogDetailVO oldValueDesc(Object oldValueDesc) {
        this.oldValueDesc = oldValueDesc;
        return this;
    }

    public BLogDetailVO newValue(Object newValue) {
        this.newValue = newValue;
        return this;
    }

    public BLogDetailVO newValueDesc(Object newValueDesc) {
        this.newValueDesc = newValueDesc;
        return this;
    }

    public BLogDetailVO prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public BLogDetailVO suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    public BLogDetailVO uptKeep(boolean uptKeep) {
        this.uptKeep = uptKeep;
        return this;
    }

    public BLogDetailVO sort(int sort) {
        this.sort = sort;
        return this;
    }

    public Object getBeforeDesc() {
        return this.getPrefix() + Optional.ofNullable(this.getOldValueDesc()).orElse("") + this.getSuffix();
    }

    public Object getAfterDesc() {
        return this.getPrefix() + Optional.ofNullable(this.getNewValueDesc()).orElse("") + this.getSuffix();
    }

    public boolean allIsNull() {
        return getOldValue() == null && getNewValue() == null;
    }

    /**
     * 属性值是否发生类变化
     *
     * @return boolean
     */
    public boolean changed() {
        if (allIsNull()) {
            return false;
        } else if (getOldValue() == null) {
            return true;
        } else {
            return !this.getOldValue().equals(getNewValue());
        }
    }

}
