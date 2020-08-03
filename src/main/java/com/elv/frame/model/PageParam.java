package com.elv.frame.model;

import java.io.Serializable;

/**
 * @author lxh
 * @since 2020-03-24
 */
public class PageParam implements Serializable {

    private static final long serialVersionUID = 5245605281239424405L;

    // 分页参数
    private int pn; // 页码
    private int ps; // 页大小
    private int maxPs = 1000; // 最大页大小
    private boolean enableMaxLimit = true; // maxPs限制是否生效

    // 游标参数
    private long cursorId; // 游标ID
    private int limitNum; // 游标页大小

    public PageParam() {
    }

    public PageParam(int pn, int ps) {
        this.pn = pn;
        this.ps = ps;
    }

    public static PageParam of(int pn, int ps) {
        return new PageParam(pn, ps);
    }

    public int getPn() {
        return pn;
    }

    public void setPn(int pn) {
        this.pn = pn;
    }

    public int getPs() {
        if (this.isEnableMaxLimit()) {
            return Math.min(ps, maxPs);
        }
        return ps;
    }

    public void setPs(int ps) {
        this.ps = ps;
    }

    public int getStart() {
        return (pn - 1) * this.getPs();
    }

    public int getMaxPs() {
        return maxPs;
    }

    public void setMaxPs(int maxPs) {
        this.maxPs = maxPs;
    }

    public boolean isEnableMaxLimit() {
        return enableMaxLimit;
    }

    public void setEnableMaxLimit(boolean enableMaxLimit) {
        this.enableMaxLimit = enableMaxLimit;
    }

    public long getCursorId() {
        return cursorId;
    }

    public void setCursorId(long cursorId) {
        this.cursorId = cursorId;
    }

    public int getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(int limitNum) {
        this.limitNum = limitNum;
    }
}
