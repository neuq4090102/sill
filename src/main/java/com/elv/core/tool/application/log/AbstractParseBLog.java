package com.elv.core.tool.application.log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.collections4.CollectionUtils;

import com.elv.core.tool.application.log.util.BLogEnum.ActionEnum;
import com.elv.core.tool.application.log.util.BLogUtil;
import com.elv.core.tool.application.log.vo.BLogCompareVO;
import com.elv.core.tool.application.log.vo.BLogGroupVO;
import com.elv.core.tool.application.log.vo.BLogVO;
import com.elv.core.util.JsonUtil;

/**
 * @author lxh
 * @since 2021-08-09
 */
public abstract class AbstractParseBLog extends AbstractBLog {

    /**
     * 换行符(日志解析使用)
     *
     * @return java.lang.String
     */
    public String newline() {
        return "<br/>\n";
    }

    /**
     * 解析日志内容
     * <p>
     * 解析请参考{@link #defaultParseLogContent(BLogVO, boolean)}
     *
     * @param logVO
     * @return java.lang.String
     */
    public abstract String parseLogContent(BLogVO logVO);

    /**
     * 解析添加日志
     *
     * @param newObject 新对象
     * @return java.lang.String 日志内容
     */
    public String parseAddLog(Object newObject) {
        return parseLogContent(createByAdd(newObject));
    }

    public BLogVO createByAdd(Object newObject) {
        ActionEnum action = ActionEnum.ADD;
        BLogCompareVO compareVO = BLogUtil.compare(null, newObject, this.careFields(), this.lang());
        List<BLogVO> logVOs = new ArrayList<>();
        for (Entry<String, BLogGroupVO> entry : compareVO.getChangedMap().entrySet()) {
            BLogGroupVO val = entry.getValue();
            List<BLogVO> subLogVOs = new ArrayList<>();
            if (val.getAfter() != null && val.getAfter() instanceof List) {
                for (Object obj : (ArrayList<?>) val.getAfter()) {
                    subLogVOs.add(this.createByAdd(obj));
                }
            }
            BLogVO logVO = new BLogVO(action, entry.getKey(), JsonUtil.toJson(val));
            logVO.setLogVOs(subLogVOs);
            logVOs.add(logVO);
        }
        return new BLogVO(action, logVOs);
    }

    /**
     * 解析删除日志
     *
     * @param oldObject 旧对象
     * @return java.lang.String 日志内容
     */
    public String parseDeleteLog(Object oldObject) {
        return parseLogContent(createByDelete(oldObject));
    }

    public BLogVO createByDelete(Object oldObject) {
        ActionEnum action = ActionEnum.DELETE;
        BLogCompareVO compareVO = BLogUtil.compare(oldObject, null, this.careFields(), this.lang());
        List<BLogVO> logVOs = new ArrayList<>();
        for (Entry<String, BLogGroupVO> entry : compareVO.getChangedMap().entrySet()) {
            BLogGroupVO val = entry.getValue();
            List<BLogVO> subLogVOs = new ArrayList<>();
            if (val.getBefore() != null && val.getBefore() instanceof List) {
                for (Object obj : (ArrayList<?>) val.getBefore()) {
                    subLogVOs.add(this.createByDelete(obj));
                }
            }
            BLogVO logVO = new BLogVO(action, entry.getKey(), JsonUtil.toJson(val));
            logVO.setLogVOs(subLogVOs);
            logVOs.add(logVO);
        }
        return new BLogVO(action, logVOs);
    }

    /**
     * 解析修改日志
     *
     * @param oldObject 旧对象
     * @param newObject 新对象
     * @return java.lang.String 日志内容
     */
    public String parseUpdateLog(Object oldObject, Object newObject) {
        return parseLogContent(createByUpdate(oldObject, newObject));
    }

    public BLogVO createByUpdate(Object oldObject, Object newObject) {
        BLogCompareVO compareVO = BLogUtil.compare(oldObject, newObject, this.careFields(), this.lang());
        if (!compareVO.changed()) { // 无变化，即使关心的属性也不解析
            return null;
        }

        ActionEnum action = ActionEnum.MODIFY;
        List<BLogVO> logVOs = new ArrayList<>();
        for (Entry<String, BLogGroupVO> entry : compareVO.getCareMap().entrySet()) {
            BLogGroupVO val = entry.getValue();
            List beforeList = new ArrayList<>();
            List afterList = new ArrayList<>();
            if (val.getBefore() != null && val.getBefore() instanceof List) {
                beforeList = (List) val.getBefore();
            }

            if (val.getAfter() != null && val.getAfter() instanceof List) {
                afterList = (List) val.getAfter();
            }

            int beforeSize = beforeList.size();
            int afterSize = afterList.size();
            int size = Math.min(beforeSize, afterSize);

            List<BLogVO> subLogVOs = new ArrayList<>();
            for (int i = 0; i < size; i++) { // 修改
                Object beforeObj = beforeList.get(i);
                Object afterObj = afterList.get(i);
                subLogVOs.add(this.createByUpdate(beforeObj, afterObj));
            }

            if (beforeSize > size) { // 删除
                for (int i = size; i < beforeSize; i++) {
                    subLogVOs.add(this.createByDelete(beforeList.get(i)));
                }
            }
            if (afterSize > size) { // 添加
                for (int i = size; i < afterSize; i++) {
                    subLogVOs.add(this.createByAdd(afterList.get(i)));
                }
            }

            BLogVO logVO = new BLogVO(action, entry.getKey(), JsonUtil.toJson(val));
            logVO.setLogVOs(subLogVOs);
            logVOs.add(logVO);
        }
        return new BLogVO(action, logVOs);
    }

    /**
     * 默认解析日志内容
     *
     * @param logVO
     * @param appendNewLine
     * @return java.lang.String
     */
    public String defaultParseLogContent(BLogVO logVO, boolean appendNewLine) {
        if (logVO == null) { // 变更在无数据变化的时候返回为null
            return "";
        }
        String newline = newline();
        ActionEnum actionEnum = ActionEnum.itemOf(logVO.getAction());
        StringBuilder sb = new StringBuilder("");

        int size = logVO.getLogVOs().size();
        for (int i = 0; i < size; i++) {
            BLogVO rowLogVO = logVO.getLogVOs().get(i);
            BLogGroupVO groupVO = JsonUtil.toObject(rowLogVO.getContent().toString(), BLogGroupVO.class);
            if (groupVO == null) {
                continue;
            }

            StringBuilder subSb = new StringBuilder();
            if (CollectionUtils.isNotEmpty(rowLogVO.getLogVOs())) { // 有子对象
                if (groupVO.getBefore() != null && groupVO.getBefore().equals(groupVO.getAfter())) {
                    continue;
                } else { // 子对象内容解析
                    sb.append(groupVO.getGroupDesc()).append("：");
                    for (BLogVO subBLogVO : rowLogVO.getLogVOs()) {
                        subSb.append("【" + subBLogVO.getActionDesc() + "】")
                                .append(defaultParseLogContent(subBLogVO, false)).append(newline);
                    }
                }
            } else {
                if (actionEnum == ActionEnum.ADD) { // 新增
                    sb.append(groupVO.getGroupDesc()).append("：").append(groupVO.getAfterDesc());
                } else if (actionEnum == ActionEnum.DELETE) { // 删除
                    sb.append(groupVO.getGroupDesc()).append("：").append(groupVO.getBeforeDesc());
                } else { // 修改 & 其他
                    if (groupVO.getBefore() != null && groupVO.getBefore().equals(groupVO.getAfter())) {
                        // 解决注解uptKeep=true的场景
                        sb.append(groupVO.getGroupDesc()).append("：").append(groupVO.getBeforeDesc());
                    } else {
                        sb.append(groupVO.getGroupDesc()).append("：").append(groupVO.getBeforeDesc()).append(" -> ")
                                .append(groupVO.getAfterDesc());
                    }
                }
            }

            if (i < size - 1 || !subSb.toString().isEmpty()) {
                if (appendNewLine) {
                    sb.append(newline);
                } else {
                    sb.append("，");
                }
                sb.append(subSb);
            }
        }
        return sb.toString();
    }
}
