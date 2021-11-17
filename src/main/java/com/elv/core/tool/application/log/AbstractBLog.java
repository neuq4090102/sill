package com.elv.core.tool.application.log;

import java.util.List;
import java.util.Map;

import com.elv.core.tool.application.log.anno.BLog;
import com.elv.core.tool.application.log.util.BLogUtil;
import com.elv.core.tool.application.log.vo.BLogCompareVO;

/**
 * 抽象业务日志（BLog=BusinessLog）
 *
 * @author lxh
 * @since 2021-08-09
 */
public abstract class AbstractBLog {

    /**
     * 关心的属性
     * <p>
     * 1.未配置，则对比所有带有注解{@link BLog}的属性
     * 2.影响展示顺序
     *
     * @return java.util.List
     */
    public List<String> careFields() {
        return null;
    }

    /**
     * 多语言
     *
     * @return java.util.Map
     */
    public Map<String, String> lang() {
        return null;
    }

    /**
     * 参数对比
     *
     * @param oldObject 旧对象
     * @param newObject 新对象
     * @return com.elv.core.tool.application.log.vo.CompareVO
     */
    public BLogCompareVO compare(Object oldObject, Object newObject) {
        return BLogUtil.compare(oldObject, newObject, this.careFields(), this.lang());
    }

}
