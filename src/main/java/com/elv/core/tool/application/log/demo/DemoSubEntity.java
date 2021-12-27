package com.elv.core.tool.application.log.demo;

import com.elv.core.tool.application.log.anno.BLog;

/**
 * @author lxh
 * @since 2021-08-09
 */
public class DemoSubEntity {

    public DemoSubEntity() {
    }

    public DemoSubEntity(long layoutId, String layoutName) {
        this.layoutId = layoutId;
        this.layoutName = layoutName;
    }

    /**
     * 房型ID
     */
    @BLog(desc = "房型ID", uptKeep = true)
    private long layoutId;
    /**
     * 房型名称
     */
    @BLog(desc = "房型名称")
    private String layoutName;

    public long getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(long layoutId) {
        this.layoutId = layoutId;
    }

    public String getLayoutName() {
        return layoutName;
    }

    public void setLayoutName(String layoutName) {
        this.layoutName = layoutName;
    }
}
