package com.elv.core.tool.application.log.vo;

import java.util.List;

import com.elv.core.tool.application.log.util.BLogEnum.ActionEnum;

/**
 * @author lxh
 * @since 2021-08-09
 */
public class BLogVO {

    /**
     * 日志操作
     *
     * @see ActionEnum
     */
    private int action;

    /**
     * 日志key
     * <p>
     * 可以是业务对象，也可以是具体的属性
     */
    private String key;

    /**
     * 日志内容
     */
    private Object content;

    /**
     * 日志明细
     */
    private List<BLogVO> logVOs;

    public BLogVO() {
    }

    public BLogVO(ActionEnum action, String key, Object content) {
        this.action = action.getValue();
        this.key = key;
        this.content = content;
    }

    public BLogVO(ActionEnum action, List<BLogVO> logVOs) {
        this.action = action.getValue();
        this.logVOs = logVOs;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public List<BLogVO> getLogVOs() {
        return logVOs;
    }

    public void setLogVOs(List<BLogVO> logVOs) {
        this.logVOs = logVOs;
    }

    public static BLogVO of() {
        return new BLogVO();
    }

    public BLogVO action(ActionEnum action) {
        this.action = action.getValue();
        return this;
    }

    public BLogVO key(String key) {
        this.key = key;
        return this;
    }

    public BLogVO content(Object content) {
        this.content = content;
        return this;
    }

    public BLogVO logVOs(List<BLogVO> logVOs) {
        this.logVOs = logVOs;
        return this;
    }

    public boolean hasValue() {
        return content != null || logVOs != null && logVOs.size() > 0;
    }

    public String getActionDesc() {
        ActionEnum actionEnum = ActionEnum.itemOf(this.getAction());
        return actionEnum == null ? "" : actionEnum.getDesc();
    }
}
