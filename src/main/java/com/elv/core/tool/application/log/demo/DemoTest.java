package com.elv.core.tool.application.log.demo;

import java.util.Map;
import java.util.Map.Entry;

import com.elv.core.tool.application.log.util.BLogEnum.ActionEnum;
import com.elv.core.tool.application.log.vo.BLogGroupVO;
import com.elv.core.tool.application.log.vo.BLogCompareVO;
import com.google.common.collect.Lists;

/**
 * @author lxh
 * @since 2021-08-09
 */
public class DemoTest {

    public static void main(String[] args) {

        DemoEntity oldEntity = createOldEntity();
        DemoEntity newEntity = createNewEntity();

        testAdd(newEntity);
        testDelete(oldEntity);
        testUpdate(oldEntity, newEntity);
        testAddLog(newEntity);
        testDeleteLog(oldEntity);
        testUpdateLog(oldEntity, newEntity);
    }

    private static void testAdd(DemoEntity newEntity) {
        System.out.println(">>>>>>>自定义-添加日志>>>>>>>>>>>>");
        BLogCompareVO compareVO = DemoLog.add(newEntity);
        echo(ActionEnum.ADD, compareVO);
    }

    private static void testAddLog(DemoEntity newEntity) {
        System.out.println(">>>>>>>添加日志>>>>>>>>>>>>");
        System.out.println(DemoLog.addLog(newEntity));
    }

    private static void testDelete(DemoEntity oldEntity) {
        System.out.println(">>>>>>>自定义-删除日志>>>>>>>>>>>>");
        BLogCompareVO compareVO = DemoLog.delete(oldEntity);
        echo(ActionEnum.DELETE, compareVO);
    }

    private static void testDeleteLog(DemoEntity oldEntity) {
        System.out.println(">>>>>>>删除日志>>>>>>>>>>>>");
        System.out.println(DemoLog.deleteLog(oldEntity));
    }

    private static void testUpdate(DemoEntity oldEntity, DemoEntity newEntity) {
        System.out.println(">>>>>>>自定义-修改日志>>>>>>>>>>>>");
        BLogCompareVO compareVO = DemoLog.update(oldEntity, newEntity);
        echo(ActionEnum.MODIFY, compareVO);
    }

    private static void testUpdateLog(DemoEntity oldEntity, DemoEntity newEntity) {
        System.out.println(">>>>>>>修改日志>>>>>>>>>>>>");
        System.out.println(DemoLog.updateLog(oldEntity, newEntity));
    }

    private static DemoEntity createOldEntity() {
        DemoEntity oldEntity = new DemoEntity();
        oldEntity.setContactNumber("1lkkjkd");
        oldEntity.setContactEmail("aba@qq.com");
        oldEntity.setContactEmail("ddd@qq.com");
        oldEntity.setStartValidity("2021-08-09");
        oldEntity.setEndValidity("2021-08-10");
        oldEntity.setType(1);
        oldEntity.setGuaranteeWay(1);
        oldEntity.setDailyRooms(7);
        oldEntity.setFloatingWeek("1,2");
        return oldEntity;
    }

    private static DemoEntity createNewEntity() {
        DemoSubEntity subEntity = new DemoSubEntity(1l, "大床房");

        DemoEntity newEntity = new DemoEntity();
        newEntity.setContactNumber("aaaa");
        newEntity.setContactEmail("ddd@qq.com");
        newEntity.setStartValidity("2021-08-01");
        newEntity.setEndValidity("2021-08-02");
        newEntity.setType(2);
        newEntity.setGuaranteeWay(2);
        newEntity.setDailyRooms(10);
        newEntity.setFloatingWeek("4,5");
        newEntity.setSubEntities(Lists.newArrayList(subEntity));
        return newEntity;
    }

    private static void echo(ActionEnum actionEnum, BLogCompareVO compareVO) {
        Map<String, BLogGroupVO> changedMap = compareVO.getCareMap();

        StringBuilder sb = new StringBuilder();
        for (Entry<String, BLogGroupVO> entry : changedMap.entrySet()) {
            BLogGroupVO value = entry.getValue();
            if (actionEnum == ActionEnum.ADD) {
                sb.append(value.getGroupDesc() + "：" + value.getAfter()).append("\n");
            } else if (actionEnum == ActionEnum.DELETE) {
                sb.append(value.getGroupDesc() + "：" + value.getBefore()).append("\n");
            } else {
                if (value.getBefore() != null && value.getBefore().equals(value.getAfter())) {
                    sb.append(value.getGroupDesc() + "：" + value.getBefore()).append("\n");
                } else {
                    sb.append(value.getGroupDesc() + "：" + value.getBefore() + " -> " + value.getAfter()).append("\n");
                }
            }
        }

        if (sb.toString().isEmpty()) {
            System.out.println("无结果");
        } else {
            System.out.println(sb.substring(0, sb.length() - 1));
        }
    }

}
