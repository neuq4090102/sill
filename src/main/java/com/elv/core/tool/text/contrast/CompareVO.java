package com.elv.core.tool.text.contrast;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

/**
 * @author lxh
 * @since 2020-08-27
 */
public class CompareVO {

    public static CompareVO of() {
        return new CompareVO();
    }

    private List<CompareDetailVO> detailVOs;

    public List<CompareDetailVO> getDetailVOs() {
        return detailVOs;
    }

    public void setDetailVOs(List<CompareDetailVO> detailVOs) {
        this.detailVOs = detailVOs;
    }

    public CompareVO detailVOs(List<CompareDetailVO> detailVOs) {
        this.detailVOs = detailVOs;
        return this;
    }

    /**
     * 对比结果map
     *
     * @return java.util.Map
     */
    public Map<String, CompareDetailVO> getCompareMap() {
        if (CollectionUtils.isEmpty(this.getDetailVOs())) {
            return Collections.emptyMap();
        }

        return this.getDetailVOs().stream().collect(Collectors.toMap(key -> key.getFieldName(), value -> value));
    }

    /**
     * 属性值已发生变化map
     *
     * @return java.util.Map
     */
    public Map<String, CompareDetailVO> getChangedMap() {
        if (CollectionUtils.isEmpty(this.getDetailVOs())) {
            return Collections.emptyMap();
        }

        return this.getDetailVOs().stream().filter(item -> item.changed())
                .collect(Collectors.toMap(key -> key.getFieldName(), value -> value));
    }

    /**
     * 属性值未发生变化map
     *
     * @return java.util.Map
     */
    public Map<String, CompareDetailVO> getUnChangedMap() {
        if (CollectionUtils.isEmpty(this.getDetailVOs())) {
            return Collections.emptyMap();
        }

        return this.getDetailVOs().stream().filter(item -> !item.changed())
                .collect(Collectors.toMap(key -> key.getFieldName(), value -> value));
    }

    /**
     * 发生变化的属性
     *
     * @return java.util.Set
     */
    public Set<String> getChangedFields() {
        return getChangedMap().keySet();
    }

    /**
     * 未发生变化的属性
     *
     * @return java.util.Set
     */
    public Set<String> getUnChangedFields() {
        return getUnChangedMap().keySet();
    }

}
