package com.elv.frame.model;

import java.io.Serializable;

/**
 * @author lxh
 * @date 2020-03-24
 */
public class PagingParam implements Serializable {

    private static final long serialVersionUID = 5245605281239424405L;

    private int pn; // 页码
    private int ps; // 页大小

    public PagingParam() {
    }

    public PagingParam(int pn, int ps) {
        this.pn = pn;
        this.ps = ps;
    }

    public static PagingParam of(int pn, int ps) {
        return new PagingParam(pn, ps);
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

    public int getStart() {
        return (pn - 1) * this.getPs();
    }
}
