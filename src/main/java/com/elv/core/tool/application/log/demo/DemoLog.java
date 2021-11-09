package com.elv.core.tool.application.log.demo;

import java.util.List;

import com.elv.core.tool.application.log.AbstractParseBLog;
import com.elv.core.tool.application.log.vo.BLogCompareVO;
import com.elv.core.tool.application.log.vo.BLogVO;

/**
 * @author lxh
 * @since 2021-08-09
 */
public class DemoLog extends AbstractParseBLog {

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
    public String newline() {
        return super.newline();
    }

    @Override
    public String parseLogContent(BLogVO logVO) {
        return super.defaultParseLogContent(logVO);
    }

    public static String addLog(Object newObject) {
        return new DemoLog().parseAddLog(newObject);
    }

    public static String deleteLog(Object oldObject) {
        return new DemoLog().parseDeleteLog(oldObject);
    }

    public static String updateLog(Object oldObject, Object newObject) {
        return new DemoLog().parseUpdateLog(oldObject, newObject);
    }

    public static BLogCompareVO add(Object newObject) {
        return new DemoLog().compare(null, newObject);
    }

    public static BLogCompareVO delete(Object oldObject) {
        return new DemoLog().compare(oldObject, null);
    }

    public static BLogCompareVO update(Object oldObject, Object newObject) {
        return new DemoLog().compare(oldObject, newObject);
    }

}
