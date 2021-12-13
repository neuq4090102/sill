package com.elv.core.tool.application.log;

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
     * 解析请参考{@link #defaultParseLogContent(BLogVO)}
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
        ActionEnum action = ActionEnum.ADD;
        BLogCompareVO compareVO = BLogUtil.compare(null, newObject, this.careFields(), this.lang());
        return parseLogContent(new BLogVO(action, compareVO.getChangedBLogVOs(action)));
    }

    /**
     * 解析删除日志
     *
     * @param oldObject 旧对象
     * @return java.lang.String 日志内容
     */
    public String parseDeleteLog(Object oldObject) {
        ActionEnum action = ActionEnum.DELETE;
        BLogCompareVO compareVO = BLogUtil.compare(oldObject, null, this.careFields(), this.lang());
        return parseLogContent(new BLogVO(action, compareVO.getChangedBLogVOs(action)));
    }

    /**
     * 解析修改日志
     *
     * @param oldObject 旧对象
     * @param newObject 新对象
     * @return java.lang.String 日志内容
     */
    public String parseUpdateLog(Object oldObject, Object newObject) {
        BLogCompareVO compareVO = BLogUtil.compare(oldObject, newObject, this.careFields(), this.lang());
        if (!compareVO.changed()) { // 无变化，即使关心的属性也不解析
            return "";
        }
        ActionEnum action = ActionEnum.MODIFY;
        return parseLogContent(new BLogVO(action, compareVO.getCareBLogVOs(action)));
    }

    /**
     * 默认解析日志内容
     *
     * @param logVO
     * @return java.lang.String
     */
    public String defaultParseLogContent(BLogVO logVO) {
        if (logVO == null) { // 变更在无数据变化的时候返回为null
            return "";
        }
        String newline = newline();
        ActionEnum actionEnum = ActionEnum.itemOf(logVO.getAction());
        StringBuilder sb = new StringBuilder();
        for (BLogVO subLogVO : logVO.getLogVOs()) {
            BLogGroupVO groupVO = JsonUtil.toObject(subLogVO.getContent().toString(), BLogGroupVO.class);
            if (groupVO == null) {
                continue;
            }
            if (actionEnum == ActionEnum.ADD) {
                sb.append(groupVO.getGroupDesc() + "：" + groupVO.getAfterDesc()).append(newline);
            } else if (actionEnum == ActionEnum.DELETE) {
                sb.append(groupVO.getGroupDesc() + "：" + groupVO.getBeforeDesc()).append(newline);
            } else if (actionEnum == ActionEnum.MODIFY) {
                if (CollectionUtils.isNotEmpty(subLogVO.getLogVOs())) {
                    // TODO:含有子对象内容的，根据需求请自行解析
                } else {
                    sb.append(groupVO.getGroupDesc() + "：" + groupVO.getBeforeDesc() + " -> " + groupVO.getAfterDesc())
                            .append(newline);
                }
            } else {
                // TODO：其他请根据需求自行解析
            }
        }
        if (sb.toString().isEmpty()) {
            return "";
        } else {
            return sb.substring(0, sb.length() - 1);
        }
    }
}
