package com.elv.core.tool.application.log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

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
     * 解析添加日志
     *
     * @param newObject 新对象
     * @return java.lang.String 日志内容
     */
    public String parseAddLog(Object newObject) {
        BLogCompareVO compareVO = BLogUtil.compare(null, newObject, this.careFields());
        int action = ActionEnum.ADD.getAction();
        List<BLogVO> logVOs = new ArrayList<>();
        for (Entry<String, BLogGroupVO> entry : compareVO.getChangedMap().entrySet()) {
            logVOs.add(new BLogVO(action, entry.getKey(), JsonUtil.toJson(entry.getValue())));
        }

        return BLogUtil.parseLogContent(new BLogVO(action, logVOs), this.newline());
    }

    /**
     * 解析删除日志
     *
     * @param oldObject 旧对象
     * @return java.lang.String 日志内容
     */
    public String parseDeleteLog(Object oldObject) {
        BLogCompareVO compareVO = BLogUtil.compare(oldObject, null, this.careFields());
        int action = ActionEnum.DELETE.getAction();
        List<BLogVO> logVOs = new ArrayList<>();
        for (Entry<String, BLogGroupVO> entry : compareVO.getChangedMap().entrySet()) {
            logVOs.add(new BLogVO(action, entry.getKey(), JsonUtil.toJson(entry.getValue())));
        }

        return BLogUtil.parseLogContent(new BLogVO(action, logVOs), this.newline());
    }

    /**
     * 解析修改日志
     *
     * @param oldObject 旧对象
     * @param newObject 新对象
     * @return java.lang.String 日志内容
     */
    public String parseUpdateLog(Object oldObject, Object newObject) {
        BLogCompareVO compareVO = BLogUtil.compare(oldObject, newObject, this.careFields());
        if (compareVO.getChangedMap().size() == 0) {
            // 无变化，即使关心的属性也不解析
            return null;
        }

        int action = ActionEnum.MODIFY.getAction();
        List<BLogVO> logVOs = new ArrayList<>();
        for (Entry<String, BLogGroupVO> entry : compareVO.getCareMap().entrySet()) { // 注意：是careMap
            logVOs.add(new BLogVO(action, entry.getKey(), JsonUtil.toJson(entry.getValue())));
        }
        if (logVOs.size() == 0) {
            return null;
        }

        return BLogUtil.parseLogContent(new BLogVO(action, logVOs), this.newline());
    }
}
