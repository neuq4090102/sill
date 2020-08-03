package com.elv.frame.model;

/**
 * 分页返回结果
 *
 * @author lxh
 * @since 2020-03-24
 */
public class PageServiceResult<T> extends ServiceResult<T> {

    private static final long serialVersionUID = 4323036511413176896L;

    private int pn = 1;
    private int ps = 20;
    private int total = 0;

    public PageServiceResult() {
    }

    public PageServiceResult(T data, int total) {
        super(data);
        this.total = total;
    }

    public int getPn() {
        return pn;
    }

    public void setPn(int pn) {
        this.pn = pn;
    }

    public int getPs() {
        return ps;
    }

    public void setPs(int ps) {
        this.ps = ps;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
