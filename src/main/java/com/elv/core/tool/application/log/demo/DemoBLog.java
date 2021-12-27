package com.elv.core.tool.application.log.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.elv.core.tool.application.log.AbstractParseBLog;
import com.elv.core.tool.application.log.vo.BLogCompareVO;
import com.elv.core.tool.application.log.vo.BLogVO;

/**
 * @author lxh
 * @since 2021-08-09
 */
public class DemoBLog extends AbstractParseBLog {

    @Override
    public List<String> careFields() {
        // List<String> list = new ArrayList<>();
        // list.add("contactNumber");
        // list.add("dailyRooms");
        // list.add("type");
        // list.add("startValidity");
        // list.add("endValidity");
        // return list;
        return null;
    }

    @Override
    public Map<String, String> lang() {
        Map<String, String> langMap = new HashMap<>();
        // langMap.put("联系电话", "联系电话");
        // langMap.put("联系邮箱", "联系邮箱");
        // langMap.put("有效开始日期", "有效开始日期");
        // langMap.put("有效结束日期", "有效结束日期");
        // langMap.put("类型", "类型");
        // langMap.put("方式", "方式");
        // langMap.put("间数", "间数");
        // langMap.put("浮动星期", "浮动星期");
        // langMap.put("子对象信息", "子对象信息");
        //
        // langMap.put("有效日期", "有效日期");
        // langMap.put("【", "【");
        // langMap.put("】", "】");
        //
        // langMap.put("联系电话", "Mobile");
        // langMap.put("联系邮箱", "Email");
        // langMap.put("有效开始日期", "Start Date");
        // langMap.put("有效结束日期", "End Date");
        // langMap.put("类型", "Type");
        // langMap.put("方式", "Way");
        // langMap.put("间数", "Count");
        // langMap.put("浮动星期", "Floating Weeking");
        // langMap.put("子对象信息", "Sub Info");
        //
        // langMap.put("有效日期", "Valid Date");
        // langMap.put("【", "[");
        // langMap.put("】", "]");
        // langMap.put("~", "--");

        // langMap.forEach((key, val) -> langMap.put(key, "中文-" + val));

        return langMap;
    }

    @Override
    public String newline() {
        return super.newline();
    }

    @Override
    public String parseLogContent(BLogVO logVO) {
        return super.defaultParseLogContent(logVO, true);
    }

    public static String addLog(Object newObject) {
        return new DemoBLog().parseAddLog(newObject);
    }

    public static String deleteLog(Object oldObject) {
        return new DemoBLog().parseDeleteLog(oldObject);
    }

    public static String updateLog(Object oldObject, Object newObject) {
        return new DemoBLog().parseUpdateLog(oldObject, newObject);
    }

    public static BLogCompareVO add(Object newObject) {
        return new DemoBLog().compare(null, newObject);
    }

    public static BLogCompareVO delete(Object oldObject) {
        return new DemoBLog().compare(oldObject, null);
    }

    public static BLogCompareVO update(Object oldObject, Object newObject) {
        return new DemoBLog().compare(oldObject, newObject);
    }

}
