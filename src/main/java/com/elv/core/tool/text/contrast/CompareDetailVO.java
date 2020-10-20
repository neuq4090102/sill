package com.elv.core.tool.text.contrast;

/**
 * 对比明细
 *
 * @author lxh
 * @since 2020-08-27
 */
public class CompareDetailVO {

    private String fieldName; // 属性名
    private Object before; // 旧的属性值
    private Object after; // 新的属性值

    public static CompareDetailVO of() {
        return new CompareDetailVO();
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getBefore() {
        return before;
    }

    public void setBefore(Object before) {
        this.before = before;
    }

    public Object getAfter() {
        return after;
    }

    public void setAfter(Object after) {
        this.after = after;
    }

    public CompareDetailVO fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public CompareDetailVO before(Object before) {
        this.before = before;
        return this;
    }

    public CompareDetailVO after(Object after) {
        this.after = after;
        return this;
    }

    public boolean allIsNull() {
        return getBefore() == null && getAfter() == null;
    }

    /**
     * 属性值是否发生类变化
     *
     * @return boolean
     */
    public boolean changed() {
        if (allIsNull()) {
            return false;
        } else if (getBefore() == null) {
            return true;
        } else {
            return !this.getBefore().equals(getAfter());
        }
    }

}
