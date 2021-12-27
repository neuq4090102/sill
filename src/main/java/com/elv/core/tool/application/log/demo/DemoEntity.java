package com.elv.core.tool.application.log.demo;

import java.util.List;

import com.elv.core.tool.application.log.anno.BLog;
import com.elv.core.tool.application.log.demo.DemoEnum.GuaranteeWayEnum;
import com.elv.core.tool.application.log.demo.DemoEnum.TypeEnum;
import com.elv.core.tool.application.log.demo.DemoEnum.WeekDay;

/**
 * @author lxh
 * @since 2021-08-09
 */
public class DemoEntity {

    /**
     * 联系电话
     */
    @BLog(desc = "联系电话")
    private String contactNumber;
    /**
     * 联系邮箱
     */
    @BLog(desc = "联系邮箱", uptKeep = true)
    private String contactEmail;
    /**
     * 有效开始日期
     */
    @Deprecated
    @BLog(desc = "有效开始日期", groupCode = "validity", groupDelimiter = " ~ ")
    private String startValidity;
    /**
     * 有效结束日期
     */
    @BLog(desc = "有效结束日期", groupCode = "validity", groupDesc = "有效日期", groupSort = 1)
    private String endValidity;
    /**
     * 类型
     */
    @BLog(desc = "类型", enumClass = TypeEnum.class, prefix = "【", suffix = "】")
    private int type;

    /**
     * 方式
     */
    @BLog(desc = "方式", enumClass = GuaranteeWayEnum.class, enumKey = "value")
    private int guaranteeWay;

    /**
     * 间数
     */
    @BLog(desc = "间数", suffix = "间")
    private int dailyRooms;

    /**
     * 浮动星期
     */
    @BLog(desc = "浮动星期", enumClass = WeekDay.class, complexEnum = true)
    private String floatingWeek;

    /**
     * 会员等级ID
     */
    @BLog(desc = "会员等级", mappingField = "vipLevelAlias")
    private long levelId;
    /**
     * 等级别名
     */
    private String vipLevelAlias;

    /**
     * 子对象信息
     */
    @BLog(desc = "子对象信息")
    private List<DemoSubEntity> subEntities;

    /**
     * 子对象信息
     */
    @BLog(desc = "子对象信息2")
    private List<DemoSubEntity> subs;

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getStartValidity() {
        return startValidity;
    }

    public void setStartValidity(String startValidity) {
        this.startValidity = startValidity;
    }

    public String getEndValidity() {
        return endValidity;
    }

    public void setEndValidity(String endValidity) {
        this.endValidity = endValidity;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getGuaranteeWay() {
        return guaranteeWay;
    }

    public void setGuaranteeWay(int guaranteeWay) {
        this.guaranteeWay = guaranteeWay;
    }

    public int getDailyRooms() {
        return dailyRooms;
    }

    public void setDailyRooms(int dailyRooms) {
        this.dailyRooms = dailyRooms;
    }

    public String getFloatingWeek() {
        return floatingWeek;
    }

    public void setFloatingWeek(String floatingWeek) {
        this.floatingWeek = floatingWeek;
    }

    public long getLevelId() {
        return levelId;
    }

    public void setLevelId(long levelId) {
        this.levelId = levelId;
    }

    public String getVipLevelAlias() {
        return vipLevelAlias;
    }

    public void setVipLevelAlias(String vipLevelAlias) {
        this.vipLevelAlias = vipLevelAlias;
    }

    public List<DemoSubEntity> getSubEntities() {
        return subEntities;
    }

    public void setSubEntities(List<DemoSubEntity> subEntities) {
        this.subEntities = subEntities;
    }

    public List<DemoSubEntity> getSubs() {
        return subs;
    }

    public void setSubs(List<DemoSubEntity> subs) {
        this.subs = subs;
    }
}
