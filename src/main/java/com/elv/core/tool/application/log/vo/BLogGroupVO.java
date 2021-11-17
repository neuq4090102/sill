package com.elv.core.tool.application.log.vo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author lxh
 * @since 2021-08-09
 */
@JsonInclude(Include.NON_NULL)
public class BLogGroupVO {

    /**
     * 是否自定义分组
     */
    private boolean customizeGroup;

    /**
     * 分组编码
     */
    private String groupCode;

    /**
     * 分组描述
     */
    private String groupDesc;

    /**
     * 分组分隔符
     */
    private String groupDelimiter;

    /**
     * 旧值
     */
    private Object before;

    /**
     * 新值
     */
    private Object after;

    /**
     * 显示顺序
     */
    private int sort;

    /**
     * 变更明细
     */
    private List<BLogDetailVO> detailVOs;

    public boolean isCustomizeGroup() {
        return customizeGroup;
    }

    public void setCustomizeGroup(boolean customizeGroup) {
        this.customizeGroup = customizeGroup;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public String getGroupDelimiter() {
        return groupDelimiter;
    }

    public void setGroupDelimiter(String groupDelimiter) {
        this.groupDelimiter = groupDelimiter;
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

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public List<BLogDetailVO> getDetailVOs() {
        return detailVOs;
    }

    public void setDetailVOs(List<BLogDetailVO> detailVOs) {
        this.detailVOs = detailVOs;
    }

    public static BLogGroupVO of() {
        return new BLogGroupVO();
    }

    public BLogGroupVO customizeGroup(boolean customizeGroup) {
        this.setCustomizeGroup(customizeGroup);
        return this;
    }

    public BLogGroupVO groupCode(String groupCode) {
        this.setGroupCode(groupCode);
        return this;
    }

    public BLogGroupVO groupDesc(String groupDesc) {
        if (this.getGroupDesc() != null && this.getGroupDesc().length() >= 0 && !this.getGroupDesc()
                .equals(this.getGroupCode())) {
            return this;
        }
        if (groupDesc == null || groupDesc.length() == 0) {
            groupDesc = this.getGroupCode();
        }

        this.setGroupDesc(groupDesc);
        return this;
    }

    public BLogGroupVO groupDelimiter(String groupDelimiter) {
        if (this.getGroupDelimiter() != null && this.getGroupDelimiter().length() > 0) {
            return this;
        }
        this.setGroupDelimiter(groupDelimiter);
        return this;
    }

    public BLogGroupVO before(Object before) {
        this.setBefore(before);
        return this;
    }

    public BLogGroupVO after(Object after) {
        this.setAfter(after);
        return this;
    }

    public BLogGroupVO sort(int sort) {
        this.setSort(sort);
        return this;
    }

    public BLogGroupVO detailVOs(List<BLogDetailVO> detailVOs) {
        this.setDetailVOs(detailVOs);
        return this;
    }

    public BLogGroupVO addDetail(BLogDetailVO detailVO) {
        if (this.getDetailVOs() == null) {
            this.setDetailVOs(new ArrayList<>());
        }
        this.getDetailVOs().add(detailVO);
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

    public boolean isUptKeep() {
        if (this.detailVOs == null || this.detailVOs.size() == 0) {
            return false;
        }
        return this.detailVOs.stream().filter(item -> item.isUptKeep()).findFirst().isPresent();
    }

}
